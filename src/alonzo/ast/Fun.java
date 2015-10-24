package alonzo.ast;

import alonzo.common.Validate;

/**
 * Function definition. An {@link ASTNode} representing the definition of a function with one
 * parameter.
 */
public class Fun extends ASTNode {

    private final Var parameter;
    private final ASTNode body;

    public Fun(final Var parameter, final ASTNode body) {
        this.parameter = Validate.notNull(parameter, "null parameter");
        this.body = Validate.notNull(body, "null body");
    }

    public Var parameter() {
        return parameter;
    }

    public ASTNode body() {
        return body;
    }
}
