package alonzo.common;

public class Validate {

    /**
     * @see #notNull(Object, String, Object...)
     */
    public static <T> T notNull(final T obj) {
        return notNull(obj, "null argument");
    }

    /**
     * Throws {@link NullPointerException} if the given object is null, returning the given object
     * if it is not null. The thrown NullPointerException will contain a message created by passing
     * msg and args to {@link String#format(String, Object...)}.
     * @param obj The object to check against null.
     * @param msg The format string to use as the exception message if obj is null.
     * @param args The arguments to msg for use if obj is null.
     * @return obj if it is not null.
     * @throws NullPointerException if obj is null.
     */
    public static <T> T notNull(final T obj, final String msg, final Object... args) {
        if (obj == null) {
            throw new NullPointerException(String.format(msg, args));
        }
        return obj;
    }
}
