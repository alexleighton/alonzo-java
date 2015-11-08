package alonzo.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import alonzo.common.Validate;

/**
 * AlonzoReader provides an interface for reading, character by character, from an underlying
 * {@link Reader}. Characters are buffered so as to provide for the efficient reading of the
 * underlying Reader.
 */
public class AlonzoReader implements AutoCloseable {

    private final Reader in;
    private boolean isClosed;

    private final CharBuffer buffer;

    private SourceLocation location;

    /**
     * The default buffer size to use.
     * @see #AlonzoReader(Reader)
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Constructs an AlonzoReader instance wrapping the given Reader and with the given buffer size.
     * @param reader The Reader to read from.
     * @param bufferSize The size of the buffer to use.
     */
    public AlonzoReader(final Reader reader, final int bufferSize) {
        in = Validate.notNull(reader, "null Reader");
        isClosed = false;

        Validate.isTrue(bufferSize > 0, "negative buffer size (%d)", bufferSize);
        buffer = CharBuffer.allocate(bufferSize);
        buffer.limit(0);

        location = SourceLocation.of(0, 1);
    }

    /**
     * Constructs an AlonzoReader with the default buffer size.
     * @see #AlonzoReader(Reader, int)
     * @see #DEFAULT_BUFFER_SIZE
     */
    public AlonzoReader(final Reader reader) {
        this(reader, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Reads a character from the underlying Reader.
     * @return a {@link ReaderResult} containing either the current character, or nothing if the
     *         end of the stream was reached.
     * @throws IOException if the underlying Reader has a problem or if this reader is already
     *                     closed.
     */
    public ReaderResult read() throws IOException {
        ensureReaderOpen();

        if (bufferEmpty()) { fillBuffer(); }
        if (bufferEmpty()) { return ReaderResult.end(location); }

        final char resultChar = buffer.get();
        final ReaderResult result = ReaderResult.of(resultChar, location);

        incrementLocation(resultChar);

        return result;
    }

    /**
     * Peeks at the next character from the underlying Reader.
     * @return a {@link ReaderResult} containing either the current character, or nothing if the
     *         end of the stream was reached.
     * @throws IOException if the underlying Reader has a problem or if this reader is already
     *                     closed.
     */
    public ReaderResult peek() throws IOException {
        ensureReaderOpen();

        if (bufferEmpty()) { fillBuffer(); }
        if (bufferEmpty()) { return ReaderResult.end(location); }

        final char resultChar = buffer.get(buffer.position());
        return ReaderResult.of(resultChar, location);
    }

    /**
     * Closes the underlying Reader. If this reader is already closed, this method does nothing.
     * @throws IOException if there was a problem closing the underlying Reader.
     */
    @Override
    public void close() throws IOException {
        if (isClosed) { return; }

        try {
            in.close();
        } finally {
            this.isClosed = true;
        }
    }

    /** @return true if this reader is closed, false otherwise. */
    public boolean isClosed() {
        return isClosed;
    }

    private void ensureReaderOpen() throws IOException {
        if (isClosed) {
            throw new IOException("Stream is closed");
        }
    }

    private boolean bufferEmpty() {
        return !buffer.hasRemaining();
    }

    private void fillBuffer() throws IOException {
        buffer.clear();

        int n = 0;
        while (n == 0) {
            n = in.read(buffer);
        }

        buffer.position(0);
        buffer.limit(n > 0 ? n : 0);
    }

    private void incrementLocation(final char result) {
        location = result == '\n' ? location.incrementLineNumber()
                                  : location.incrementCharacter();
    }
}