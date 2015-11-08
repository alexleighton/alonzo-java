package alonzo;

import alonzo.ast.fmt.FormatterTest;
import alonzo.common.ValidateTest;
import alonzo.io.AlonzoReaderTest;
import alonzo.io.ReaderResultTest;
import alonzo.io.SourceLocationTest;
import alonzo.io.StubReaderTest;
import alonzo.unit.TestRunner;

public class TestingMain {

    public static void main(final String[] args) {
        new TestRunner()
            .withTest(FormatterTest.class)
            .withTest(ValidateTest.class)
            .withTest(StubReaderTest.class)
            .withTest(AlonzoReaderTest.class)
            .withTest(ReaderResultTest.class)
            .withTest(SourceLocationTest.class)
            .run();
    }

}
