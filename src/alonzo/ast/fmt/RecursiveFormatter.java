package alonzo.ast.fmt;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import alonzo.ast.ASTNode;
import alonzo.ast.App;
import alonzo.ast.Fun;
import alonzo.ast.Var;

/**
 * {@link Formatter} implemented as a naive recursive function.
 */
public class RecursiveFormatter extends Formatter {

    public RecursiveFormatter() {
        super(DEFAULT_CHARSET);
    }

    public RecursiveFormatter(final Charset charset) {
        super(charset);
    }

    @Override
    public void format(final OutputStream out, final ASTNode root) throws IOException {
        if (root.isVar()) {
            out.write(bytes(((Var) root).name()));
        }

        if (root.isFun()) {
            formatFun(out, (Fun) root);
        }

        if (root.isApp()) {
            formatApp(out, (App) root);
        }
    }

    private void formatFun(final OutputStream out, final Fun fun) throws IOException {
        out.write(bytes("(λ"));
        format(out, fun.parameter());
        out.write(bytes("."));
        format(out, fun.body());
        out.write(bytes(")"));
    }

    private void formatApp(final OutputStream out, final App app) throws IOException {
        out.write(bytes("("));
        format(out, app.function());
        out.write(bytes(" "));
        format(out, app.argument());
        out.write(bytes(")"));
    }

}
