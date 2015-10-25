package alonzo.ast.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import alonzo.ast.ASTNode;
import alonzo.common.Validate;
import alonzo.exc.UnexpectedIssueException;

/**
 * Abstract superclass of any {@link ASTNode} parsers.
 */
public abstract class Parser {

    /**
     * Parser defaults to assuming UTF8 as the charset for read strings.
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Charset charset;

    protected Parser(final Charset charset) {
        this.charset = Validate.notNull(charset, "null charset");
    }

    /**
     * Parses an {@code ASTNode} from the given {@code InputStream}. All characters are read from
     * {@code in} using the {@link Charset} this class was constructed with.
     * @param in The {@code InputStream} to read the {@code ASTNode} from.
     * @return the parsed {@code ASTNode}.
     * @throws IOException if {@code in} had an issue being read from.
     */
    public abstract ASTNode parse(final InputStream in) throws IOException;

    /**
     * @return an {@code ASTNode} parsed from the given {@code String}.
     * @see #parse(InputStream)
     */
    public ASTNode parse(final String input) {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(input));

        try {
            return parse(in);
        } catch (final IOException e) {
            throw new UnexpectedIssueException(
                "This shouldn't happen, as ByteArrayInputStream shouldn't throw IOException.", e);
        }
    }

    protected byte[] bytes(final String s) {
        return s.getBytes(charset);
    }
}
