package alonzo.ast.fmt;

import static alonzo.unit.Assert.assertEquals;
import static alonzo.unit.Assert.fail;
import alonzo.ast.App;
import alonzo.ast.Fun;
import alonzo.ast.Var;
import alonzo.ast.fmt.Formatter;
import alonzo.ast.fmt.IterativeFormatter;
import alonzo.ast.fmt.RecursiveFormatter;
import alonzo.unit.Ignore;
import alonzo.unit.Test;

public class FormatterTest {

    private final Formatter iterFmt = new IterativeFormatter();
    private final Formatter recFmt = new RecursiveFormatter();

    @Test
    public void fmtVar() {
        final Var v = new Var("testVariableName");

        assertEquals("testVariableName", iterFmt.format(v));
        assertEquals("testVariableName", recFmt.format(v));
    }

    @Test
    public void fmtFun() {
        final Fun f = new Fun(new Var("var"), new Var("body"));

        assertEquals("(λvar.body)", iterFmt.format(f));
        assertEquals("(λvar.body)", recFmt.format(f));
    }

    @Test
    public void fmtApp() {
        final App a = new App(new Fun(new Var("x"), new Var("x")), new Var("y"));

        assertEquals("((λx.x) y)", iterFmt.format(a));
        assertEquals("((λx.x) y)", recFmt.format(a));
    }

    @Test
    public void fmtComplicated() {
        final Fun sub = new Fun(new Var("x"), new App(new Var("f"), new App(new Var("x"), new Var("x"))));
        final Fun ycombinator = new Fun(new Var("f"), new App(sub, sub));

        assertEquals("(λf.((λx.(f (x x))) (λx.(f (x x)))))", iterFmt.format(ycombinator));
        assertEquals("(λf.((λx.(f (x x))) (λx.(f (x x)))))", recFmt.format(ycombinator));
    }

    @Ignore // Ignored because the size of the call stack is an implementation detail. To observe
            // the test in action, you'll need to adjust the depth of nesting to suit your JVM.
    @Test
    public void overflow() throws Exception {
        Fun fun = new Fun(new Var("x"), new Var("x"));
        String formatted = "(λx.x)";

        for (int i = 0; i < 10000; ++i) {
            fun = new Fun(new Var("x"), fun);
            formatted = "(λx." + formatted + ")";
        }

        assertEquals(formatted, iterFmt.format(fun));

        try {
            recFmt.format(fun);
            fail("Expected StackOverflowError.");
        } catch (final StackOverflowError exc) {}
    }
}
