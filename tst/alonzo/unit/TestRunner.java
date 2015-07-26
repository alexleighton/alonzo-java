package alonzo.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Top-level Test runner. Add classes containing {@link Test} annotated methods to this TestRunner
 * {@link #withTest(Class)}, and when you're ready to run the suite, call {@link #run()}.
 */
public class TestRunner {

    private final List<RunnableTest> testClasses;

    /**
     * Construct a new TestRunner.
     */
    public TestRunner() {
        testClasses = new ArrayList<>();
    }

    /**
     * Add all tests in {@code clazz} to this runner.
     * @param clazz The Class containing tests to be run.
     * @return this TestRunner.
     */
    public TestRunner withTest(final Class<?> clazz) {
        this.testClasses.addAll(RunnableTest.of(clazz));
        return this;
    }

    /**
     * Runs all tests added to this TestRunner, collecting and printing the results to Standard out.
     */
    public void run() {
        final List<TestResult> results = testClasses.stream()
            .map(r -> r.run())
            .collect(Collectors.toList());

        final List<TestResult> passed  = results.stream()
            .filter(r -> r.isPassed()).collect(Collectors.toList());
        final List<TestResult> failed  = results.stream()
            .filter(r -> r.isFailed()).collect(Collectors.toList());
        final List<TestResult> errored = results.stream()
            .filter(r -> r.isErrored()).collect(Collectors.toList());
        final List<TestResult> ignored = results.stream()
            .filter(r -> r.isIgnored()).collect(Collectors.toList());

        for (final TestResult result: failed) {
            System.out.println(String.format("Failed test \"%s\"", result.getTestName()));
            result.asFailed().getAssertionFailed().printStackTrace();
            System.out.println();
        }

        for (final TestResult result: errored) {
            System.out.println(String.format("Errored test \"%s\"", result.getTestName()));
            result.asErrored().getCause().printStackTrace();
            System.out.println();
        }

        final boolean overallFailure = failed.size() + errored.size() > 0;

        System.out.println(String.format(
                "Test Results: %s\n%d Total [%d Passed, %d Failed, %d Errors, %d Ignored]",
                overallFailure ? "FAILURE" : "PASS",
                results.size(), passed.size(), failed.size(), errored.size(), ignored.size()));
    }

}
