package alonzo.ast;

/**
 * Abstract superclass for all classes that represent an Abstract Syntax Tree node.
 * @see Var
 * @see Fun
 * @see App
 */
public abstract class ASTNode {

    public boolean isVar() {
        return this instanceof Var;
    }

    public boolean isFun() {
        return this instanceof Fun;
    }

    public boolean isApp() {
        return this instanceof App;
    }

}
