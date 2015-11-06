package alonzo.io;

import static alonzo.unit.Assert.assertEquals;
import static alonzo.unit.Assert.assertFalse;
import static alonzo.unit.Assert.assertTrue;
import static alonzo.unit.Assert.fail;

import java.io.IOException;

import alonzo.unit.Test;

public class StubReaderTest {

    @Test
    public void read() throws IOException {
        try (final StubReader reader = new StubReader.Builder("first", "", "last").build()) {
            final char[] buffer = new char[5];

            int numberOfCharacters = reader.read(buffer);
            assertEquals("first", new String(buffer));
            assertEquals(buffer.length, numberOfCharacters);

            numberOfCharacters = reader.read(buffer);
            assertEquals(0, numberOfCharacters);

            numberOfCharacters = reader.read(buffer);
            assertEquals("last", new String(buffer, 0, 4));
            assertEquals(4, numberOfCharacters);

            numberOfCharacters = reader.read(buffer);
            assertEquals(-1, numberOfCharacters);
        }
    }

    @Test
    public void partialRead() throws IOException {
        try (final StubReader reader = new StubReader.Builder("first", "last").build()) {
            final char[] buffer = new char[5];

            int numberOfCharacters = reader.read(buffer, 0, 3);
            assertEquals("fir", new String(buffer, 0, 3));
            assertEquals(3, numberOfCharacters);

            // Partial spanning results
            numberOfCharacters = reader.read(buffer, 0, 6);
            assertEquals("st", new String(buffer, 0, numberOfCharacters));
            assertEquals(2, numberOfCharacters);

            assertEquals('l', reader.read());

            numberOfCharacters = reader.read(buffer, 0, 2);
            assertEquals("as", new String(buffer, 0, 2));
            assertEquals(2, numberOfCharacters);

            // Partial spanning last result and end of input
            numberOfCharacters = reader.read(buffer, 0, 2);
            assertEquals("t", new String(buffer, 0, numberOfCharacters));
            assertEquals(1, numberOfCharacters);

            numberOfCharacters = reader.read(buffer);
            assertEquals(-1, numberOfCharacters);
        }
    }

    @Test
    @SuppressWarnings("resource")
    public void close() throws IOException {
        final StubReader reader = new StubReader.Builder().build();
        assertFalse(reader.isClosed());

        reader.close();
        assertTrue(reader.isClosed());
    }

    @Test
    @SuppressWarnings("resource")
    public void closeThrowingException() {
        final StubReader reader = new StubReader.Builder().withCloseException().build();
        try {
            reader.close();
            fail("Expected IOException.");
        } catch (IOException e) {
            assertEquals("Close IOException", e.getMessage());
        }
    }
}
