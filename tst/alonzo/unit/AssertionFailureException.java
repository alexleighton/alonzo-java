package alonzo.unit;

/**
 * An exception used to indicate Assertion failure. The message should have some indication as to
 * why the assertion failed.
 */
public class AssertionFailureException extends RuntimeException {

    private static final long serialVersionUID = 6493753070232384600L;

    public AssertionFailureException() {
        super();
    }

    public AssertionFailureException(final String message) {
        super(message);
    }

    public AssertionFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
