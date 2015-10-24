package alonzo.exc;

/**
 * Exception representing an unexpected issue. In one sense, the fact that we're throwing this
 * exception is an indication that the issue was expected. However, this is basically a catch-all
 * for any problem that's not expected to actually occur (thus unexpected).
 *
 * This exception helpfully directs the receiver of the exception to this project's issues page
 * where they can file an issue explaining how the got this {@code UnexpectedIssueException}.
 */
public class UnexpectedIssueException extends RuntimeException {

    private static final long serialVersionUID = 9120629908786986083L;

    private static final String ISSUES_MESSAGE =
        " [Please consider filing an issue: https://github.com/alexleighton/alonzo-java/issues]";

    public UnexpectedIssueException(String fmtString, Object... args) {
        this(null, fmtString, args);
    }

    public UnexpectedIssueException(Exception exc, String fmtString, Object... args) {
        this(String.format(fmtString, args), exc);
    }

    public UnexpectedIssueException(String msg, Exception exc) {
        super(msg + ISSUES_MESSAGE, exc);
    }

}
