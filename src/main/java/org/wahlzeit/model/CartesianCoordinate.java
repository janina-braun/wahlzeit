package org.wahlzeit.model;

import java.util.ArrayList;
import java.util.Objects;

import static org.wahlzeit.model.AssertUtils.*;

public class CartesianCoordinate extends AbstractCoordinate {
    private double x;
    private double y;
    private double z;

    public CartesianCoordinate(double x, double y, double z) {
        assertValidDouble(x);
        assertValidDouble(y);
        assertValidDouble(z);
        setX(x);
        setY(y);
        setZ(z);
        assertClassInvariants();
    }

    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public double getCartesianDistance(Coordinate coordinate) {
        assertArgumentNotNull(coordinate);
        assertClassInvariants();
        CartesianCoordinate c = coordinate.asCartesianCoordinate();
        double d_x = Math.pow(x - c.getX(), 2); //(x1 - x2)^2
        double d_y = Math.pow(y - c.getY(), 2); //(y1 - y2)^2
        double d_z = Math.pow(z - c.getZ(), 2); //(z1 - z2)^2
        double distance = Math.sqrt(d_x + d_y + d_z);
        assertValidDouble(distance);
        assertNotNegative(distance);
        assertClassInvariants();
        return distance;
    }

    public SphericCoordinate asSphericCoordinate() throws ArithmeticException {
        assertClassInvariants();
        double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        //if radius=0, theta and phi must be 0 too
        //prevents division by zero
        if ( radius <= tolerance) {
            return new SphericCoordinate(0, 0, 0);
        }
        double theta = Math.acos(z / radius);
        double phi = Math.atan2(y, x);
        SphericCoordinate spheric = new SphericCoordinate(phi, theta, radius);
        //SphericCoordinate Constructor executes assertClassInvariants for spheric
        assertClassInvariants();
        return spheric;
    }

    @Override
    public boolean isEqual(Coordinate coordinate) {
        assertArgumentNotNull(coordinate);
        assertClassInvariants();
        CartesianCoordinate c = coordinate.asCartesianCoordinate();
        boolean equal = false;
        if (Math.abs(x - c.getX()) <= tolerance) {
            if (Math.abs(y - c.getY()) <= tolerance) {
                equal = Math.abs(z - c.getZ()) <= tolerance;
            }
        }
        assertClassInvariants();
        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    //get coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public void doReadFrom(int type, double c1, double c2, double c3) {
        if (type == 0) { //type cartesian
            //coordinates of the ResultSet are of type cartesian
            setX(c1);
            setY(c2);
            setZ(c3);
            //setter execute assertValidDouble
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric and have to be converted before saving
            CartesianCoordinate c = new SphericCoordinate(c1, c2, c3).asCartesianCoordinate();
            //CartesianCoordinate Constructor in asCartesianCoordinate executes assertClassInvariants for c
            setX(c.getX());
            setY(c.getY());
            setZ(c.getZ());
            //setter execute assertValidDouble
        }
    }

    public ArrayList<Number> doWriteOn() {
        //writes coordinate type and the values of the three coordinates into the ArrayList
        ArrayList<Number> values = new ArrayList<>();
        values.add(0);
        values.add(getX());
        values.add(getY());
        values.add(getZ());
        return values;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        assertValidDouble(x);
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        assertValidDouble(y);
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        assertValidDouble(z);
        this.z = z;
    }

    @Override
    protected void assertClassInvariants() {
        assertValidDouble(x);
        assertValidDouble(y);
        assertValidDouble(z);
    }
}
