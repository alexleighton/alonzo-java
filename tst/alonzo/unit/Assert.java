package alonzo.unit;

/**
 * Assertion helper functions live here.
 */
public class Assert {

    /**
     * Checks that the given boolean expression is true, throwing {@link AssertionFailureException}
     * if it is not true.
     * @param booleanExpr The boolean expression being asserted on.
     * @throws AssertionFailureException if booleanExpr is false.
     */
    public static void assertTrue(final boolean booleanExpr) {
        if (!booleanExpr) {
            throw new AssertionFailureException();
        }
    }

    /**
     * Checks that the given boolean expression is false, throwing {@link AssertionFailureException}
     * if it is true.
     * @param booleanExpr The boolean expression being asserted on.
     * @throws AssertionFailureException if booleanExpr is true.
     */
    public static void assertFalse(final boolean booleanExpr) {
        if (booleanExpr) {
            throw new AssertionFailureException();
        }
    }

    /**
     * Compares expected with actual for equality, throwing {@link AssertionFailureException} if
     * they are not equal. This function is null-safe.
     * @param expected The expected value of actual. May be {@code null}.
     * @param actual   The actual value. May be {@code null}.
     * @throws AssertionFailureException if expected does not equal actual.
     */
    public static void assertEquals(final Object expected, final Object actual) {
        if (expected == actual) { return; }

        if (expected == null || actual == null || !expected.equals(actual)) {
            throw new AssertionFailureException(failureMessage(expected, actual));
        }
    }

    /** @see #assertEquals(Object, Object) */
    public static void assertEquals(final int expected, final int actual) {
        assertEquals((Integer) expected, (Integer) actual);
    }

    private static String failureMessage(final Object expected, final Object actual) {
        return "Expected: <" + expected + ">, actual: <" + actual + ">";
    }

    /**
     * Fails the current test, using the given message as the reason. The given message and args are
     * passed to {@link String#format(String, Object...)}.
     * @param message The reason the test failed.
     * @param args The format-string arguments to message.
     * @throws AssertionFailureException with the given message.
     */
    public static void fail(String message, Object... args) {
        throw new AssertionFailureException(String.format(message, args));
    }

}
