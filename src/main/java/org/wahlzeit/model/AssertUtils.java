package org.wahlzeit.model;

public class AssertUtils {
    public static void assertValidDouble(double value) {
        //Double.isFinite returns false if double is infinite or NaN
        if (!Double.isFinite(value)) {
            throw new IllegalStateException("Double value is infinite or not a number.");
        }
    }

    public static void assertNotNegative(double value) {
        if (value < 0) {
            throw new IllegalStateException("This value cannot be negative.");
        }
    }

    public static void assertArgumentNotNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
    }
}
