package alonzo;

import static alonzo.unit.Assert.assertEquals;
import alonzo.unit.Test;

public class FooTest {

    @Test
    public void firstTest() {
        assertEquals(0, new Foo().bar());
    }
}
