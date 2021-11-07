package org.wahlzeit.model;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CoordinateTest {
    @Test
    public void testGetDistance() {
        Coordinate coordinate1 = new Coordinate(1,2,3);
        Coordinate coordinate2 = new Coordinate(3,2,1);
        double distance = coordinate1.getDistance(coordinate2);
        assertEquals(Math.sqrt(8), distance, 0.000001);
    }

    @Test
    public void testIsEqual() {
        Coordinate coordinate1 = new Coordinate(1,2,3);
        Coordinate coordinate2 = new Coordinate(3,2,1);
        Coordinate coordinate3 = new Coordinate(3,2,1);
        Boolean isNotEqual = coordinate1.isEqual(coordinate2);
        Boolean isEqual = coordinate2.isEqual(coordinate3);

        assertTrue(isEqual);
        assertFalse(isNotEqual);
    }

    @Test
    public void testEquals() {
        Coordinate coordinate1 = new Coordinate(1,2,3);
        Coordinate coordinate2 = new Coordinate(3,2,1);
        Coordinate coordinate3 = new Coordinate(3,2,1);
        Location location = new Location(coordinate1);
        Boolean notEqual = coordinate1.equals(coordinate2);
        Boolean equal = coordinate2.equals(coordinate3);
        Boolean wrongClass = coordinate1.equals(location);
        Boolean nullPointer = coordinate1.equals(null);

        assertTrue(equal);
        assertFalse(notEqual);
        assertFalse(wrongClass);
        assertFalse(nullPointer);
    }

}
