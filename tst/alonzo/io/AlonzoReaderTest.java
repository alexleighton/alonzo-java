package alonzo.io;

import static alonzo.unit.Assert.assertEquals;
import static alonzo.unit.Assert.assertFalse;
import static alonzo.unit.Assert.assertTrue;
import static alonzo.unit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

import alonzo.unit.Test;

public class AlonzoReaderTest {

    @Test
    public void basicRead() throws IOException {
        final String sourceString = "testCharacters";
        final StringReader stringReader = new StringReader(sourceString);

        try (final AlonzoReader reader = new AlonzoReader(stringReader)) {
            assertEquals('t', (char) reader.read());
        }
    }

    @Test
    @SuppressWarnings("resource")
    public void close() throws IOException {
        final AlonzoReader reader = new AlonzoReader(new StringReader("testSource"));
        assertFalse(reader.isClosed());

        reader.close();
        assertTrue(reader.isClosed());

        try {
            reader.read();
            fail("Expected IOException.");
        } catch (final IOException e) {
            assertEquals("Stream is closed", e.getMessage());
        }
    }

    @Test
    @SuppressWarnings("resource")
    public void closeThrowingException() {
        final StubReader stub = new StubReader.Builder().withCloseException().build();

        final AlonzoReader reader = new AlonzoReader(stub);
        try {
            reader.close();
            fail("Expected IOException.");
        } catch (final IOException e) {
            assertTrue(reader.isClosed());
        }
    }

    @Test
    public void endOfStream() throws IOException {
        try (final AlonzoReader reader = new AlonzoReader(new StringReader("t"))) {
            assertEquals('t', reader.read());
            assertEquals(-1, reader.read());
        }
    }

    @Test
    public void readUntilBufferIsFilled() throws IOException {
        final StubReader stub = new StubReader.Builder("", "t").build();

        try (final AlonzoReader reader = new AlonzoReader(stub)) {
            assertEquals('t', reader.read());
            assertEquals(-1, reader.read());
        }
    }

    @Test
    public void nullReaderConstruction() throws IOException {
        try (final AlonzoReader reader = new AlonzoReader(null)){
            fail("Expected NPE");
        } catch (final NullPointerException e) {}
    }

    @Test
    public void invalidBufferSizeConstruction() throws IOException {
        try (final AlonzoReader reader = new AlonzoReader(new StringReader(""), -1)) {
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            assertEquals("negative buffer size (-1)", e.getMessage());
        }
    }
}
