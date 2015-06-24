package alonzo.unit;


/**
 * Abstract class representing the result of a test.
 * @see Passed
 * @see Failed
 * @see Errored
 */
public abstract class TestResult {

    /**
     * Construct a {@link Passed} test result.
     * @param testName The name of the test that passed.
     * @return A Passed test result.
     */
    public static Passed pass(final String testName) {
        return new Passed(testName);
    }

    /**
     * Construct a {@link Failed} test result.
     * @param testName The name of the test that failed.
     * @param e        The assertion failure that caused the test to fail.
     * @return A Failed test result.
     */
    public static Failed fail(final String testName, final AssertionFailureException e)
    {
        return new Failed(testName, e);
    }

    /**
     * Construct an {@link Errored} test result.
     * @param testName The name of the test that had an error.
     * @param cause    The Throwable that caused the test to error.
     * @return An Errored test result.
     */
    public static Errored error(final String testName, final Throwable cause)
    {
        return new Errored(testName, cause);
    }

    private final String testName;

    private TestResult(final String testName) {
        this.testName = testName;
    }

    /** @return the name of the test this result is for. */
    public String getTestName() {
        return testName;
    }

    /** @return true if this is a {@link Passed} result, false otherwise. */
    public boolean isPassed() {
        return this instanceof Passed;
    }

    /** @return true if this is a {@link Failed} result, false otherwise. */
    public boolean isFailed() {
        return this instanceof Failed;
    }

    /** @return true if this is an {@link Errored} result, false otherwise. */
    public boolean isErrored() {
        return this instanceof Errored;
    }

    /** @return {@code this} casted to {@link Passed}. Safe if guarded by {@link #isPassed()}. */
    public Passed asPassed() {
        return (Passed) this;
    }

    /** @return {@code this} casted to {@link Failed}. Safe if guarded by {@link #isFailed()}. */
    public Failed asFailed() {
        return (Failed) this;
    }

    /** @return {@code this} casted to {@link Errored}. Safe if guarded by {@link #isErrored()}. */
    public Errored asErrored() {
        return (Errored) this;
    }

    /**
     * Objects representing a passing test result for given tests.
     */
    public static class Passed extends TestResult {
        private Passed(final String testName) {
            super(testName);
        }
    }

    /**
     * Objects representing a failed test result for given tests.
     */
    public static class Failed extends TestResult {
        private final AssertionFailureException assertExc;

        private Failed(final String testName, final AssertionFailureException assertExc) {
            super(testName);
            this.assertExc = assertExc;
        }

        /** @return The {@link AssertionFailureException} that caused a test to fail. */
        public AssertionFailureException getAssertionFailed() {
            return assertExc;
        }
    }

    /**
     * Objects representing an errored test result for given tests.
     */
    public static class Errored extends TestResult {
        private final Throwable cause;

        private Errored(final String testName, final Throwable cause) {
            super(testName);
            this.cause = cause;
        }

        /** @return The Throwable that caused a test to error. */
        public Throwable getCause() {
            return cause;
        }
    }
}
