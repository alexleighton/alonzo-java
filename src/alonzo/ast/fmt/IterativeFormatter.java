package alonzo.ast.fmt;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Stack;

import alonzo.ast.ASTNode;
import alonzo.ast.App;
import alonzo.ast.Fun;
import alonzo.ast.Var;
import alonzo.exc.UnexpectedIssueException;

/**
 * {@link Formatter} implemented iteratively so as not to blow the function call stack on too-deeply
 * nested {@link ASTNode} structures.
 */
public class IterativeFormatter extends Formatter {

    public IterativeFormatter() {
        super(Formatter.DEFAULT_CHARSET);
    }

    public IterativeFormatter(final Charset charset) {
        super(charset);
    }

    @Override
    public void format(final OutputStream out, final ASTNode root) throws IOException {
        final Stack<ASTContext> contexts = new Stack<>();
        contexts.push(ASTContext.initial(root));

        while (!contexts.isEmpty()) {
            final ASTContext ctx = contexts.pop();

            if (ctx.node() instanceof Var) {
                out.write(bytes(((Var) ctx.node()).name()));
            }

            if (ctx.node() instanceof Fun) {
                formatFun(out, contexts, (Fun) ctx.node(), ctx.state());
            }

            if (ctx.node() instanceof App) {
                formatApp(out, contexts, (App) ctx.node(), ctx.state());
            }
        }
    }

    private void formatFun(final OutputStream out,
                           final Stack<ASTContext> contexts,
                           final Fun fun,
                           final int state)
    throws IOException
    {
        switch (state) {
        case ASTContext.BEFORE_NODE:
            out.write(bytes("(λ"));
            contexts.push(ASTContext.of(fun, ASTContext.AFTER_PARAMETER));
            contexts.push(ASTContext.initial(fun.parameter()));
            break;
        case ASTContext.AFTER_PARAMETER:
            out.write(bytes("."));
            contexts.push(ASTContext.of(fun, ASTContext.AFTER_BODY));
            contexts.push(ASTContext.initial(fun.body()));
            break;
        case ASTContext.AFTER_BODY:
            out.write(bytes(")"));
            break;
        default: throw new UnexpectedIssueException("Unexpected state: \"" + state + "\".");
        }
    }

    private void formatApp(final OutputStream out,
                           final Stack<ASTContext> contexts,
                           final App app,
                           final int state)
    throws IOException
    {
        switch (state) {
        case ASTContext.BEFORE_NODE:
            out.write(bytes("("));
            contexts.push(ASTContext.of(app, ASTContext.AFTER_LEFT));
            contexts.push(ASTContext.initial(app.function()));
            break;
        case ASTContext.AFTER_LEFT:
            out.write(bytes(" "));
            contexts.push(ASTContext.of(app, ASTContext.AFTER_RIGHT));
            contexts.push(ASTContext.initial(app.argument()));
            break;
        case ASTContext.AFTER_RIGHT:
            out.write(bytes(")"));
            break;
        default: throw new UnexpectedIssueException("Unexpected status: \"" + state + "\".");
        }
    }

    private static class ASTContext {
        static final int BEFORE_NODE = 0;
        static final int AFTER_PARAMETER = 1;
        static final int AFTER_BODY = 2;
        static final int AFTER_LEFT = 3;
        static final int AFTER_RIGHT = 4;

        private final ASTNode node;
        private final int state;

        public static ASTContext initial(final ASTNode node) {
            return new ASTContext(node, BEFORE_NODE);
        }

        public static ASTContext of(final ASTNode node, final int state) {
            return new ASTContext(node, state);
        }

        private ASTContext(final ASTNode node, final int state) {
            this.node = node;
            this.state = state;
        }

        public ASTNode node() {
            return node;
        }

        public int state() {
            return state;
        }
    }
}
