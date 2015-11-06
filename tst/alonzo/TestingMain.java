package alonzo;

import alonzo.ast.fmt.FormatterTest;
import alonzo.common.ValidateTest;
import alonzo.io.StubReaderTest;
import alonzo.unit.TestRunner;

public class TestingMain {

    public static void main(String[] args) {
        new TestRunner()
            .withTest(FormatterTest.class)
            .withTest(ValidateTest.class)
            .withTest(StubReaderTest.class)
            .run();
    }

}
