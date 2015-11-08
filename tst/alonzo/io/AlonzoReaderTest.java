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
    public void read() throws IOException {
        final String sourceString = "testCharacters";
        final StringReader stringReader = new StringReader(sourceString);

        try (final AlonzoReader reader = new AlonzoReader(stringReader)) {
            final ReaderResult result = reader.read();
            assertEquals('t', result.get());
            assertEquals(0, result.location().character());
            assertEquals(1, result.location().line());
        }
    }

    @Test
    public void peek() throws IOException {
        final StubReader stub = new StubReader.Builder("12").build();
        try (final AlonzoReader reader = new AlonzoReader(stub)) {
            assertEquals('1', reader.peek().get());
            assertEquals(SourceLocation.of(0, 1), reader.peek().location());
            assertEquals('1', reader.peek().get());
            assertEquals(SourceLocation.of(0, 1), reader.peek().location());
            assertEquals('1', reader.read().get());

            assertEquals('2', reader.peek().get());
            assertEquals('2', reader.read().get());
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
            assertEquals('t', reader.read().get());

            ReaderResult result = reader.read();
            assertTrue(result.endOfStream());
            assertEquals(SourceLocation.of(1, 1), result.location());

            // End of stream results in the same location
            result = reader.read();
            assertTrue(result.endOfStream());
            assertEquals(SourceLocation.of(1, 1), result.location());
        }
    }

    @Test
    public void readUntilBufferIsFilled() throws IOException {
        final StubReader stub = new StubReader.Builder("", "t").build();

        try (final AlonzoReader reader = new AlonzoReader(stub)) {
            assertEquals('t', reader.read().get());
            assertTrue(reader.read().endOfStream());
        }
    }

    @Test
    public void newlineIncrementsLineNumber() throws IOException {
        final StubReader stub = new StubReader.Builder("test\nnextLine\r\ntheEnd").build();
        try (final AlonzoReader reader = new AlonzoReader(stub)) {
            ReaderResult result = reader.read();
            assertEquals(SourceLocation.of(0, 1), result.location());

            for (int i = 0; i < 3; ++i) { reader.read(); }

            // Newline character is on the same "line" as previous characters.
            result = reader.read();
            assertEquals('\n', result.get());
            assertEquals(SourceLocation.of(4, 1), result.location());

            // Next character gets the next line number.
            result = reader.read();
            assertEquals('n', result.get());
            assertEquals(SourceLocation.of(0, 2), result.location());

            for (int i = 0; i < 7; ++i) { reader.read(); }

            // '/r' is not treated specially.
            result = reader.read();
            assertEquals('\r', result.get());
            assertEquals(SourceLocation.of(8, 2), result.location());

            result = reader.read();
            assertEquals('\n', result.get());
            assertEquals(SourceLocation.of(9, 2), result.location());

            result = reader.read();
            assertEquals('t', result.get());
            assertEquals(SourceLocation.of(0, 3), result.location());
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
