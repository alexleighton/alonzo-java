package alonzo;

import alonzo.ast.fmt.FormatterTest;
import alonzo.unit.TestRunner;

public class TestingMain {

    public static void main(String[] args) {
        new TestRunner()
            .withTest(FooTest.class)
            .withTest(FormatterTest.class)
            .run();
    }

}
