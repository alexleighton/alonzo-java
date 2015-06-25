package alonzo;

import alonzo.unit.TestRunner;

public class TestingMain {

    public static void main(String[] args) {
        new TestRunner()
            .withTest(FooTest.class)
            .run();
    }

}
