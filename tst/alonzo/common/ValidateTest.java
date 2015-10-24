package alonzo.common;

import static alonzo.unit.Assert.assertEquals;
import static alonzo.unit.Assert.assertTrue;
import static alonzo.unit.Assert.fail;
import alonzo.unit.Test;

public class ValidateTest {

    @Test
    public void notNullReturnsObject() {
        final Object subject = new Object();
        final Object result = Validate.notNull(subject);

        assertEquals(subject, result);
        assertTrue(subject == result);
    }

    @Test
    public void notNullThrowsNullPointerException() {
        try {
            Validate.notNull(null);
            fail("Expected NullPointerException.");
        } catch (NullPointerException e) {
            assertEquals("null argument", e.getMessage());
        }
    }

    @Test
    public void notNullThrowsNullPointerExceptionWithMessage() {
        try {
            Validate.notNull(null, "null with message");
            fail("Expected NullPointerException.");
        } catch (NullPointerException e) {
            assertEquals("null with message", e.getMessage());
        }
    }
}
