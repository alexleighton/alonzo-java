package alonzo.io;

import static alonzo.unit.Assert.assertEquals;
import alonzo.unit.Test;

public class SourceLocationTest {

    @Test
    public void construction() {
        final SourceLocation locA = SourceLocation.of(0, 1);
        final SourceLocation locB = locA;
        assertEquals(locA, locB);
        assertEquals(locA, SourceLocation.of(0, 1));

        assertEquals(locA.hashCode(), SourceLocation.of(0, 1).hashCode());
        assertEquals(locA.hashCode(), locB.hashCode());

        assertEquals(locA.toString(), SourceLocation.of(0, 1).toString());
        assertEquals(locA.toString(), locB.toString());
        assertEquals("SourceLocation(0,1)", locA.toString());
    }

    @Test
    public void access() {
        final SourceLocation loc = SourceLocation.of(0, 1);

        assertEquals(0, loc.character());
        assertEquals(1, loc.line());
    }

    @Test
    public void incrementCharacter() {
        final SourceLocation loc = SourceLocation.of(0, 2).incrementCharacter();

        assertEquals(1, loc.character());
        assertEquals(2, loc.line());
    }

    @Test
    public void incrementLine() {
        final SourceLocation loc = SourceLocation.of(5, 1).incrementLineNumber();

        assertEquals(0, loc.character());
        assertEquals(2, loc.line());
    }

}
