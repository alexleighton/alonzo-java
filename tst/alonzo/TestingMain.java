package alonzo;

import alonzo.ast.fmt.FormatterTest;
import alonzo.common.ValidateTest;
import alonzo.unit.TestRunner;

public class TestingMain {

    public static void main(String[] args) {
        new TestRunner()
            .withTest(FooTest.class)
            .withTest(FormatterTest.class)
            .withTest(ValidateTest.class)
            .run();
    }

}
