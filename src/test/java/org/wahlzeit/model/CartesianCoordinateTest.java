package org.wahlzeit.model;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;

import static org.junit.Assert.*;

public class CartesianCoordinateTest {
    final double tolerance = 0.000001;
    CartesianCoordinate coordinate1 = null;
    CartesianCoordinate coordinate2 = null;
    CartesianCoordinate coordinate3 = null;
    SphericCoordinate s_coordinate1 = null;
    SphericCoordinate s_coordinate2 = null;
    SphericCoordinate s_coordinate3 = null;
    Location location = null;

    @Before
    public void setUp() {
        coordinate1 = new CartesianCoordinate(1,2,3);
        coordinate2 = new CartesianCoordinate(3,2,1);
        coordinate3 = new CartesianCoordinate(3,2,1);
        s_coordinate1 = new SphericCoordinate(1.10714871779409, 0.640522312679425,  3.74165738677394); //equals coordinate1
        s_coordinate2 = new SphericCoordinate(0.588002603547568, 1.30024656381632, 3.74165738677394); //equals coordinate2
        s_coordinate3 = new SphericCoordinate(0.588002603547568, 1.30024656381632, 3.74165738677394); //equals coordinate3
        location = new Location(coordinate1);
    }

    @Test
    public void testAsCartesianCoordinate() {
        CartesianCoordinate c = coordinate1.asCartesianCoordinate();
        assertEquals(coordinate1, c); //cartesian to cartesian
    }

    @Test
    public void testGetCartesianDistance() {
        double distance1 = coordinate1.getCartesianDistance(coordinate2);
        double distance2 = coordinate1.getCartesianDistance(s_coordinate2);
        assertEquals(Math.sqrt(8), distance1, tolerance); //cartesian and cartesian
        assertEquals(Math.sqrt(8), distance2, tolerance); //cartesian and spheric
    }

    @Test
    public void testAsSphericCoordinate() {
        SphericCoordinate s = coordinate2.asSphericCoordinate();
        assertEquals(s_coordinate2, s); //cartesian to spheric
    }

    @Test
    public void testGetCentralAngle() {
        double angle1 = coordinate1.getCentralAngle(s_coordinate2);
        double angle2 = coordinate1.getCentralAngle(coordinate2);
        double expect = Math.acos(Math.sin(1.10714871779409)*Math.sin(0.588002603547568) + Math.cos(1.10714871779409)*Math.cos(0.588002603547568)*Math.cos(Math.abs(1.30024656381632-0.640522312679425)));
        assertEquals(expect, angle1, tolerance); //cartesian and spheric
        assertEquals(expect, angle2, tolerance); //cartesian and cartesian
    }

    @Test
    public void testIsEqual() {
        Boolean isNotEqual1 = coordinate1.isEqual(coordinate2);
        Boolean isEqual1 = coordinate2.isEqual(coordinate3);
        Boolean isNotEqual2 = coordinate1.isEqual(s_coordinate2);
        Boolean isEqual2 = coordinate2.isEqual(s_coordinate3);

        assertTrue(isEqual1); //cartesian and cartesian
        assertFalse(isNotEqual1); //cartesian and cartesian
        assertTrue(isEqual2); //cartesian and spheric
        assertFalse(isNotEqual2); //cartesian and spheric
    }

    @Test
    public void testEquals() {
        Boolean notEqual1 = coordinate1.equals(coordinate2);
        Boolean equal1 = coordinate2.equals(coordinate3);
        Boolean isNotEqual2 = coordinate1.equals(s_coordinate2);
        Boolean isEqual2 = coordinate2.equals(s_coordinate3);
        Boolean wrongClass = coordinate1.equals(location);
        Boolean nullPointer = coordinate1.equals(null);

        assertTrue(equal1); //cartesian and cartesian
        assertFalse(notEqual1); //cartesian and cartesian
        assertTrue(isEqual2); //cartesian and spheric
        assertFalse(isNotEqual2); //cartesian and spheric
        assertFalse(wrongClass);
        assertFalse(nullPointer);
    }

    @Test
    public void testHashCode() {
        int hash1 = coordinate1.hashCode();
        int hash2 = coordinate2.hashCode();
        int hash3 = coordinate3.hashCode();

        assertEquals(hash2, hash3);
        assertNotEquals(hash1, hash2);
    }
}
