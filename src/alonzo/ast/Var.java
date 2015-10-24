package alonzo.ast;

import alonzo.common.Validate;

/**
 * Variable reference. An {@link ASTNode} representing a referenced variable.
 */
public class Var extends ASTNode {

    private final String name;

    public Var(final String name) {
        this.name = Validate.notNull(name, "null name");
    }

    public String name() {
        return name;
    }

}
