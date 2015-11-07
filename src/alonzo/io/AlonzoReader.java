package alonzo.io;

import java.io.IOException;
import java.io.Reader;

import alonzo.common.Validate;

/**
 * AlonzoReader provides an interface for reading, character by character, from an underlying
 * {@link Reader}. Characters are buffered so as to provide for the efficient reading of the
 * underlying Reader.
 */
public class AlonzoReader implements AutoCloseable {

    private final Reader in;
    private boolean isClosed;

    private final char[] buffer;
    private int length;
    private int index;

    /**
     * The default buffer size to use.
     * @see #AlonzoReader(Reader)
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Constructs an AlonzoReader instance wrapping the given Reader and with the given buffer size.
     * @param in The Reader to read from.s
     * @param bufferSize The size of the buffer to use.
     */
    public AlonzoReader(final Reader in, final int bufferSize) {
        this.in = Validate.notNull(in, "null Reader");
        this.isClosed = false;

        Validate.isTrue(bufferSize > 0, "negative buffer size (%d)", bufferSize);
        this.buffer = new char[bufferSize];
        this.index = 0;
        this.length = 0;
    }

    /**
     * Constructs an AlonzoReader with the default buffer size.
     * @see #AlonzoReader(Reader, int)
     * @see #DEFAULT_BUFFER_SIZE
     */
    public AlonzoReader(final Reader in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Reads a character from the underlying Reader.
     * @return a character from the underlying Reader, or -1 if there is no more input.
     * @throws IOException if the underlying Reader has a problem or if this reader is already
     *                     closed.
     */
    public int read() throws IOException {
        if (isClosed) {
            throw new IOException("Stream is closed");
        }

        if (bufferEmpty()) { fillBuffer(); }
        if (bufferEmpty()) { return -1; }

        return buffer[index++];
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

    private void fillBuffer() throws IOException {
        int n = 0;
        while (n == 0) {
            n = in.read(buffer, 0, buffer.length);
        }

        if (n > 0) {
            length = n;
            index = 0;
        }
    }

    private boolean bufferEmpty() {
        return index >= length;
    }

}