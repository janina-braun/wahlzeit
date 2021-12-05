package org.wahlzeit.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class SphericCoordinateTest {
    final double tolerance = 0.000001;
    CartesianCoordinate coordinate0 = null;
    CartesianCoordinate coordinate1 = null;
    CartesianCoordinate coordinate2 = null;
    CartesianCoordinate coordinate3 = null;
    SphericCoordinate s_coordinate0 = null;
    SphericCoordinate s_coordinate1 = null;
    SphericCoordinate s_coordinate2 = null;
    SphericCoordinate s_coordinate3 = null;
    Location location = null;

    @Before
    public void setUp() {
        coordinate0 = new CartesianCoordinate(0,0,0);
        coordinate1 = new CartesianCoordinate(1,2,3);
        coordinate2 = new CartesianCoordinate(3,2,1);
        coordinate3 = new CartesianCoordinate(3,2,1);
        s_coordinate0 = new SphericCoordinate(0,0,0);
        s_coordinate1 = new SphericCoordinate(1.10714871779409, 0.640522312679425,  3.74165738677394); //equals coordinate1
        s_coordinate2 = new SphericCoordinate(0.588002603547568, 1.30024656381632, 3.74165738677394); //equals coordinate2
        s_coordinate3 = new SphericCoordinate(0.588002603547568, 1.30024656381632, 3.74165738677394); //equals coordinate3
        location = new Location(coordinate1);
    }

    //Throws an IllegalArgumentException if radius <= 0.0
    @Test(expected = IllegalStateException.class)
    public void testRadiusIsNegative() {
        //negative radius is a illegal argument
        //radius must be >= 0.0
        new SphericCoordinate(1, 1, -1);
    }

    @Test
    public void testRadiusIsZero() {
        SphericCoordinate s = new SphericCoordinate(1, 1, 0.0);
        //if radius = 0.0, theta and phi are also 0.0
        assertEquals(s_coordinate0, s);
    }

    @Test
    public void testAsCartesianCoordinate() {
        CartesianCoordinate c = s_coordinate1.asCartesianCoordinate();
        assertEquals(coordinate1, c); //spheric to cartesian
    }

    @Test
    public void testOriginAsSphericCoordinate() {
        CartesianCoordinate c = s_coordinate0.asCartesianCoordinate();
        assertEquals(coordinate0, c); //spheric to cartesian
    }

    @Test
    public void testGetCartesianDistance() {
        double distance1 = s_coordinate1.getCartesianDistance(coordinate2);
        double distance2 = s_coordinate1.getCartesianDistance(s_coordinate2);
        assertEquals(Math.sqrt(8), distance1, tolerance); //spheric and cartesian
        assertEquals(Math.sqrt(8), distance2, tolerance); //spheric and spheric
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCartesianDistanceNullPointer() {
        s_coordinate1.getCartesianDistance(null);
    }

    @Test
    public void testAsSphericCoordinate() {
        SphericCoordinate s = s_coordinate2.asSphericCoordinate();
        assertEquals(s_coordinate2, s); //spheric to spheric
    }

    @Test
    public void testGetCentralAngle() {
        double angle1 = s_coordinate1.getCentralAngle(s_coordinate2);
        double angle2 = s_coordinate1.getCentralAngle(coordinate2);
        double expect = Math.acos(Math.sin(1.10714871779409)*Math.sin(0.588002603547568) + Math.cos(1.10714871779409)*Math.cos(0.588002603547568)*Math.cos(Math.abs(1.30024656381632-0.640522312679425)));
        assertEquals(expect, angle1, tolerance); //spheric and spheric
        assertEquals(expect, angle2, tolerance); //spheric and cartesian
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCentralAngleNullPointer() {
        s_coordinate1.getCentralAngle(null);
    }

    @Test
    public void testIsEqual() {
        Boolean isNotEqual1 = s_coordinate1.isEqual(coordinate2);
        Boolean isEqual1 = s_coordinate2.isEqual(coordinate3);
        Boolean isNotEqual2 = s_coordinate1.isEqual(s_coordinate2);
        Boolean isEqual2 = s_coordinate2.isEqual(s_coordinate3);

        assertTrue(isEqual1); //spheric and cartesian
        assertFalse(isNotEqual1); //spheric and cartesian
        assertTrue(isEqual2); //spheric and spheric
        assertFalse(isNotEqual2); //spheric and spheric
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEqualNullPointer() {
        s_coordinate1.isEqual(null);
    }

    @Test
    public void testEquals() {
        Boolean notEqual1 = s_coordinate1.equals(coordinate2);
        Boolean equal1 = s_coordinate2.equals(coordinate3);
        Boolean isNotEqual2 = s_coordinate1.equals(s_coordinate2);
        Boolean isEqual2 = s_coordinate2.equals(s_coordinate3);
        Boolean wrongClass = s_coordinate1.equals(location);
        Boolean nullPointer = s_coordinate1.equals(null);

        assertTrue(equal1); //spheric and cartesian
        assertFalse(notEqual1); //spheric and cartesian
        assertTrue(isEqual2); //cartesian and spheric
        assertFalse(isNotEqual2); //cartesian and spheric
        assertFalse(wrongClass);
        assertFalse(nullPointer);
    }

    @Test
    public void testHashCode() {
        int hash1 = s_coordinate1.hashCode();
        int hash2 = s_coordinate2.hashCode();
        int hash3 = s_coordinate3.hashCode();

        assertEquals(hash2, hash3);
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testDoWriteOn() {
        ArrayList<Number> values = s_coordinate1.doWriteOn();
        assertEquals(1, values.get(0));
        assertEquals(s_coordinate1.getPhi(), values.get(1));
        assertEquals(s_coordinate1.getTheta(), values.get(2));
        assertEquals(s_coordinate1.getRadius(), values.get(3));
    }
}
