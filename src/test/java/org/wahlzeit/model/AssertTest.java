package org.wahlzeit.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.wahlzeit.model.coordinate.AbstractCoordinate.*;
import static org.wahlzeit.model.AssertUtils.*;
import static org.wahlzeit.model.coordinate.SphericCoordinate.*;

public class AssertTest {
    @Test(expected = IllegalStateException.class)
    public void testAssertNotNaN() {
        assertValidDouble(1.0/0);
    }

    @Test(expected = IllegalStateException.class)
    public void testAssertNotInfiniteDouble() {
        assertValidDouble(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testAssertNotNegative() {
        assertNotNegative(-0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertArgumentNotNull() {
        assertArgumentNotNull(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertArrayListSize() {
        ArrayList<Number> values = new ArrayList<>();
        values.add(1);
        values.add(2.3);
        values.add(4.5);
        assertWriteOnArrayListSize(values);
    }

    @Test(expected = ArithmeticException.class)
    public void testMinAngle() {
        assertAngle(-1.2);
    }

    @Test(expected = ArithmeticException.class)
    public void testMaxAngle() {
        assertAngle(360.1);
    }







}
