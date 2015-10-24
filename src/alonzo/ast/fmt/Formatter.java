package alonzo.ast.fmt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import alonzo.ast.ASTNode;
import alonzo.common.Validate;
import alonzo.exc.UnexpectedIssueException;

/**
 * Abstract superclass of any {@link ASTNode} formatters.
 */
public abstract class Formatter {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Charset charset;

    public Formatter(final Charset charset) {
        this.charset = Validate.notNull(charset, "null charset");
    }

    /**
     * Format the given {@code ASTNode}, outputting to the given {@code OutputStream}. All character
     * data is formatted to {@code out} using the {@link Charset} this object was constructed with.
     * @param out  The {@code OutputStream} to format {@code root} to.
     * @param root The {@code ASTNode} to format.
     * @throws IOException if {@code out} had an issue being written to.
     */
    public abstract void format(final OutputStream out, final ASTNode root) throws IOException;

    /**
     * @return a formatted string of the given {@link ASTNode} root node.
     */
    public String format(final ASTNode root) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            format(out, root);
        } catch (IOException e) {
            throw new UnexpectedIssueException(
                "This shouldn't happen, as ByteArrayOutputStream shouldn't throw IOException.", e);
        }

        return new String(out.toByteArray(), charset);
    }

    protected byte[] bytes(final String name) {
        return name.getBytes(charset);
    }
}
