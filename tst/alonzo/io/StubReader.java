package alonzo.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;

import alonzo.common.Validate;

/**
 * Stub class for allowing controlled behavior of a Reader.
 *
 * This class allows the user to stub-out the responses of the read operation to simulate certain
 * {@link Reader} behaviors, like e.g. a call to {@link #read(char[], int, int)} that reads no
 * characters into the output buffer.
 *
 * Construct an instance of StubReader using the provided {@link Builder} class.
 */
public class StubReader extends Reader {

    private final LinkedList<String> readResults;
    private final boolean doCloseException;

    private boolean isClosed;

    /**
     * Creates a StubReader instance that will output the given results.
     * @param results The list of results to return from {@link #read(char[], int, int)}.
     * @param doCloseException Whether to throw an IOException from {@link #close()} or not.
     */
    private StubReader(final LinkedList<String> results, final boolean doCloseException) {
        this.readResults = Validate.notNull(results, "null read results");
        this.doCloseException = doCloseException;

        this.isClosed = false;
    }

    /**
     * "Closes" this reader.
     * @throws IOException Does not throw IOException.
     */
    @Override
    public void close() throws IOException {
        readResults.clear();
        isClosed = true;

        if (doCloseException) {
            throw new IOException("Close IOException");
        }
    }

    /**
     * Reads the current result into the output buffer. If the read request is for less characters
     * than the total characters in the current result, the current result is split up into 2
     * partials. The first partial is returned (copied into the output buffer), and the second is
     * put back into the queue, to be returned for the next read request.
     *
     * @param outBuffer The buffer to read result characters into.
     * @param outOffset The offset into {@code outBuffer} to start at.
     * @param outLength The length of result characters to read.
     * @return the number of characters read. -1 if there are no more results to return.
     * @throws IOException if this reader is closed.
     */
    @Override
    public int read(final char[] outBuffer, final int outOffset, final int outLength)
    throws IOException
    {
        if (isClosed) { throw new IOException("Stream is closed."); }
        if (noMoreResults()) { return -1; }

        final String currentResult = readResults.poll();
        final int copyLength = Math.min(outLength, currentResult.length());

        currentResult.getChars(0, copyLength, outBuffer, outOffset);

        final String partial = currentResult.substring(copyLength, currentResult.length());
        if (partial.length() > 0) {
            readResults.push(partial);
        }

        return copyLength;
    }

    /** @return true if the reader is closed, false otherwise. */
    public boolean isClosed() {
        return isClosed;
    }

    private boolean noMoreResults() {
        return readResults.peek() == null;
    }

    /** Builder for StubReader. */
    public static class Builder {
        private final LinkedList<String> readResults;
        private boolean doCloseException = false;

        /**
         * Constructs a Builder for an instance of StubReader.
         * @param results The results to return from calls to
         *                {@link StubReader#read(char[], int, int)}
         */
        public Builder(final String... results) {
            this.readResults = new LinkedList<>();

            readResults.addAll(Arrays.asList(results));
        }

        /**
         * Indicates that the constructed StubReader should throw an {@link IOException} when close
         * is called.
         * @return this, for chaining Builder calls.
         */
        public Builder withCloseException() {
            doCloseException = true;
            return this;
        }

        /** @return the built StubReader. */
        public StubReader build() {
            return new StubReader(readResults, doCloseException);
        }
    }
}
