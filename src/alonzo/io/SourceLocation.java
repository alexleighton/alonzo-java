package alonzo.io;

import java.util.Objects;

public class SourceLocation {

    private final int character;
    private final int line;

    public static SourceLocation of(final int character, final int line) {
        return new SourceLocation(character, line);
    }

    private SourceLocation(final int character, final int line) {
        this.character = character;
        this.line = line;
    }

    public int character() {
        return character;
    }

    public int line() {
        return line;
    }

    public SourceLocation incrementCharacter() {
        return new SourceLocation(character+1, line);
    }

    public SourceLocation incrementLineNumber() {
        return new SourceLocation(0, line+1);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof SourceLocation)) { return false; }

        final SourceLocation other = (SourceLocation) obj;
        return character == other.character
            &&      line == other.line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, line);
    }

    @Override
    public String toString() {
        return String.format("SourceLocation(%d,%d)", character, line);
    }
}
