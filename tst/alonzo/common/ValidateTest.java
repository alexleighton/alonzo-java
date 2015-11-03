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
            Validate.notNull(null, "null with message %s", "and arg");
            fail("Expected NullPointerException.");
        } catch (NullPointerException e) {
            assertEquals("null with message and arg", e.getMessage());
        }
    }

    @Test
    public void isFalse() {
        try {
            Validate.isFalse(false);
        } catch (Throwable t) {
            fail("Expected no exception to be thrown. Got %s: %s",
                 t.getClass().getSimpleName(), t.getMessage());
        }
    }

    @Test
    public void isFalseThrowsIllegalArgumentException() {
        try {
            Validate.isFalse(true);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("true expression", e.getMessage());
        }
    }

    @Test
    public void isFalseThrowsIllegalArgumentExceptionWithMessage() {
        try {
            Validate.isFalse(true, "true with message %s", "and arg");
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("true with message and arg", e.getMessage());
        }
    }

}
