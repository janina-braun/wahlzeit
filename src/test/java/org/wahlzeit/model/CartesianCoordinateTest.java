package org.wahlzeit.model;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.model.coordinate.CartesianCoordinate;
import org.wahlzeit.model.coordinate.SphericCoordinate;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CartesianCoordinateTest {
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
        coordinate0 = CartesianCoordinate.ensureCartesianCoordinate(0,0,0);
        coordinate1 = CartesianCoordinate.ensureCartesianCoordinate(1,2,3);
        coordinate2 = CartesianCoordinate.ensureCartesianCoordinate(3,2,1);
        coordinate3 = CartesianCoordinate.ensureCartesianCoordinate(3,2,1);
        s_coordinate0 = SphericCoordinate.ensureSphericCoordinate(0,0,0);
        s_coordinate1 = SphericCoordinate.ensureSphericCoordinate(1.10714871779409, 0.640522312679425,  3.74165738677394); //equals coordinate1
        s_coordinate2 = SphericCoordinate.ensureSphericCoordinate(0.588002603547568, 1.30024656381632, 3.74165738677394); //equals coordinate2
        s_coordinate3 = SphericCoordinate.ensureSphericCoordinate(0.588002603547568, 1.30024656381632, 3.74165738677394); //equals coordinate3
        location = new Location(coordinate1);
    }

    @Test
    public void testAsCartesianCoordinate() {
        CartesianCoordinate c = coordinate1.asCartesianCoordinate();
        assertEquals(coordinate1, c); //cartesian to cartesian
    }

    @Test
    public void testGetCartesianDistance() throws WrongCalculationException {
        double distance1 = coordinate1.getCartesianDistance(coordinate2);
        double distance2 = coordinate1.getCartesianDistance(s_coordinate2);
        assertEquals(Math.sqrt(8), distance1, tolerance); //cartesian and cartesian
        assertEquals(Math.sqrt(8), distance2, tolerance); //cartesian and spheric
    }

    @Test(expected = WrongCalculationException.class)
    public void testGetCartesianDistanceNullPointer() throws WrongCalculationException {
        coordinate1.getCartesianDistance(null);
    }

    @Test
    public void testAsSphericCoordinate() {
        SphericCoordinate s = coordinate2.asSphericCoordinate();
        assertEquals(s_coordinate2, s); //cartesian to spheric
    }

    @Test
    public void testOriginAsSphericCoordinate() {
        SphericCoordinate s = coordinate0.asSphericCoordinate();
        assertEquals(s_coordinate0, s); //cartesian to spheric
    }

    @Test
    public void testGetCentralAngle() throws WrongCalculationException {
        double angle1 = coordinate1.getCentralAngle(s_coordinate2);
        double angle2 = coordinate1.getCentralAngle(coordinate2);
        double expect = Math.acos(Math.sin(1.10714871779409)*Math.sin(0.588002603547568) + Math.cos(1.10714871779409)*Math.cos(0.588002603547568)*Math.cos(Math.abs(1.30024656381632-0.640522312679425)));
        assertEquals(expect, angle1, tolerance); //cartesian and spheric
        assertEquals(expect, angle2, tolerance); //cartesian and cartesian
    }

    @Test(expected = WrongCalculationException.class)
    public void testGetCentralAngleNullPointer() throws WrongCalculationException {
        coordinate1.getCentralAngle(null);
    }

    @Test
    public void testIsEqual() throws WrongCalculationException {
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
    public void testIsEqualNullPointer() throws WrongCalculationException {
        Boolean isNotEqual = coordinate1.isEqual(null);
        assertFalse(isNotEqual);
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

    @Test
    public void testDoWriteOn() {
        ArrayList<Number> values = coordinate1.doWriteOn();
        assertEquals(0, values.get(0));
        assertEquals(coordinate1.getX(), values.get(1));
        assertEquals(coordinate1.getY(), values.get(2));
        assertEquals(coordinate1.getZ(), values.get(3));
    }
}
