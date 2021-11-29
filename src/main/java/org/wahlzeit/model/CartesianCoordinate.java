package org.wahlzeit.model;

import java.util.ArrayList;
import java.util.Objects;

public class CartesianCoordinate extends AbstractCoordinate {
    private double x;
    private double y;
    private double z;

    public CartesianCoordinate(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    public SphericCoordinate asSphericCoordinate() throws ArithmeticException {
        double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        //if radius=0, theta and phi must be 0 too
        //prevents division by zero
        if ( radius <= tolerance) {
            return new SphericCoordinate(0, 0, 0);
        }
        double theta = Math.acos(z / radius);
        double phi = Math.atan2(y, x);
        return new SphericCoordinate(phi, theta, radius);
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
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric and have to be converted before saving
            CartesianCoordinate c = new SphericCoordinate(c1, c2, c3).asCartesianCoordinate();
            setX(c.getX());
            setY(c.getY());
            setZ(c.getZ());
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
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
