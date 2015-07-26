package alonzo.unit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import alonzo.exc.UnexpectedIssueException;

/**
 * Container used for holding and running the objects necessary for a test.
 * @see TestRunner for the actual runner-of-tests.
 */
public class RunnableTest implements Supplier<TestResult> {

    /**
     * Constructs all RunnableTests from the given class. A class containing RunnableTests is a
     * class with a no-parameter constructor containing one or more {@link Test} annotated methods.
     * The methods themselves must also take no parameters.
     *
     * @param clazz The class to scan for runnable tests.
     * @return A collection of RunnableTests, one for each {@link Test} annotated method.
     */
    public static Collection<RunnableTest> of(Class<?> clazz) {
        final Constructor<?> constructor = findNoParameterConstructor(clazz);
        final List<Method> testMethods = findTestMethods(clazz);

        return testMethods.stream()
            .map(method -> new RunnableTest(clazz, constructor, method))
            .collect(Collectors.toList());
    }

    private final Class<?> clazz;
    private final Constructor<?> constructor;
    private final Method method;
    private final String testName;

    private RunnableTest(final Class<?> clazz,
                         final Constructor<?> constructor,
                         final Method method)
    {
        this.clazz = clazz;
        this.constructor = constructor;
        this.method = method;
        this.testName = testName(clazz, method);
    }

    /** @see #get() */
    public TestResult run() {
        return get();
    }

    /**
     * Run the contained test, determining if it passes, fails, or errors. A test is thought to
     * pass if the called test method returns successfully. If an {@link AssertionFailureException}
     * is thrown from the test method, the test is considered to have failed. If any other
     * {@link Exception} is thrown, the test is considered to have errored.
     * @return the result of running the test.
     */
    @Override
    public TestResult get() {
        if (isIgnoredTest()) {
            return TestResult.ignore(testName);
        }

        final Object instance = constructTestInstance();

        try {
            method.invoke(instance);

            return TestResult.pass(testName);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AssertionFailureException) {
                return TestResult.fail(testName, (AssertionFailureException) e.getCause());
            } else {
                return TestResult.error(testName, e.getCause());
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new UnexpectedIssueException("Could not invoke test method \""
                                               + testName + "\".", e);
        }
    }

    private boolean isIgnoredTest() {
        return method.getAnnotation(Ignore.class) != null;
    }

    private Object constructTestInstance() {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | IllegalArgumentException | InvocationTargetException e)
        {
            throw new UnexpectedIssueException("Could not create an instance of test class \""
                                               + clazz.getName() + "\".", e);
        }
    }

    private static Constructor<?> findNoParameterConstructor(final Class<?> clazz) {
        final Optional<Constructor<?>> constructor = Arrays.stream(clazz.getConstructors())
            .filter(c -> c.getParameterCount() == 0)
            .findFirst();

        if (constructor.isPresent()) {
            return constructor.get();
        } else {
            throw new IllegalArgumentException(
                String.format("Could not find no-parameter constructor for class \"%s\"",
                              clazz.getName()));
        }
    }

    private static List<Method> findTestMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(m -> m.getAnnotation(Test.class) != null && m.getParameterCount() == 0)
            .collect(Collectors.toList());
    }

    private static String testName(final Class<?> testClazz, final Method testMethod) {
        return testClazz.getName() + "." + testMethod.getName();
    }
}
