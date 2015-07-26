package alonzo.ast;

/**
 * Function application. An {@link ASTNode} representing the application of a function to one
 * argument.
 */
public class App extends ASTNode {

    private final ASTNode function;
    private final ASTNode argument;

    public App(final ASTNode function, final ASTNode argument) {
        this.function = function;
        this.argument = argument;
    }

    public ASTNode function() {
        return function;
    }

    public ASTNode argument() {
        return argument;
    }

}
