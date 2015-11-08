package alonzo.io;

import java.util.NoSuchElementException;
import java.util.Optional;

import alonzo.common.Validate;

public class ReaderResult {

    private final Optional<Character> result;
    private final SourceLocation location;

    public static ReaderResult of(final char result,
                                  final SourceLocation location)
    {
        return new ReaderResult(Optional.of(result), location);
    }

    public static ReaderResult end(final SourceLocation location) {
        return new ReaderResult(Optional.empty(), location);
    }

    public ReaderResult(final Optional<Character> resultChar,
                        final SourceLocation loc)
    {
        result = Validate.notNull(resultChar, "null result");
        location = Validate.notNull(loc, "null SourceLocation");
    }

    public char get() {
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NoSuchElementException("End of stream.");
        }
    }

    public boolean endOfStream() {
        return !result.isPresent();
    }

    public SourceLocation location() {
        return location;
    }
}
