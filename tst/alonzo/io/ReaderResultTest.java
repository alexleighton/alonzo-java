package alonzo.io;

import static alonzo.unit.Assert.assertEquals;
import static alonzo.unit.Assert.assertFalse;
import static alonzo.unit.Assert.assertTrue;

import java.util.NoSuchElementException;

import alonzo.unit.Test;

public class ReaderResultTest {

    @Test
    public void get() {
        assertEquals('c', ReaderResult.of('c', SourceLocation.of(0, 0)).get());

        try {
            ReaderResult.end(SourceLocation.of(0, 0)).get();
        } catch (final NoSuchElementException e) {
            assertEquals("End of stream.", e.getMessage());
        }
    }

    @Test
    public void endOfStream() {
        assertFalse(ReaderResult.of('c', SourceLocation.of(0, 0)).endOfStream());

        assertTrue(ReaderResult.end(SourceLocation.of(0, 0)).endOfStream());
    }
}
