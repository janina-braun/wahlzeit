package org.wahlzeit.model;

import java.util.ArrayList;

import static org.wahlzeit.model.AssertUtils.*;

public class SphericCoordinate extends AbstractCoordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) throws IllegalArgumentException{
        assertValidDouble(phi);
        assertValidDouble(theta);
        assertValidDouble(radius);
        assertNotNegative(radius);
        if (radius <= tolerance) {
            //if radius = 0.0, theta and phi are also 0.0
            this.phi = 0.0;
            this.theta = 0.0;
            this.radius = 0.0;
        } else {
            this.phi = phi;
            this.theta = theta;
            this.radius = radius;
        }
        assertClassInvariants();
    }

    public CartesianCoordinate asCartesianCoordinate() throws ArithmeticException {
        assertClassInvariants();
        double x = radius * Math.cos(phi) * Math.sin(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(theta);
        CartesianCoordinate cartesian = new CartesianCoordinate(x, y, z);
        //CartesianCoordinate Constructor executes assertClassInvariants for cartesian
        assertClassInvariants();
        return cartesian;
    }

    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCentralAngle(Coordinate coordinate) {
        assertArgumentNotNull(coordinate);
        assertClassInvariants();
        SphericCoordinate s = coordinate.asSphericCoordinate();
        double part1 = Math.sin(phi) * Math.sin(s.getPhi());
        double deltaTheta = Math.abs(s.getTheta() - theta);
        double part2 = Math.cos(phi) * Math.cos(s.getPhi()) * Math.cos(deltaTheta);
        double centralAngle = Math.acos(part1 + part2);
        assertValidDouble(centralAngle);
        assertAngle(centralAngle);
        assertClassInvariants();
        return centralAngle;
    }

    //get coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public void doReadFrom(int type, double c1, double c2, double c3) {
        if (type == 0) { //type cartesian
            //Coordinates of the ResultSet are of type cartesian and have to be converted before saving
            SphericCoordinate s = new CartesianCoordinate(c1, c2, c3).asSphericCoordinate();
            //SphericCoordinate Constructor in asSphericCoordinate executes assertClassInvariants for s
            setPhi(s.getPhi());
            setTheta(s.getTheta());
            setRadius(s.getRadius());
            //setter execute assertValidDouble
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric
            setPhi(c1);
            setTheta(c2);
            setRadius(c3);
            //setter execute assertValidDouble
        }
    }

    public ArrayList<Number> doWriteOn() {
        //writes coordinate type and the values of the three coordinates into the ArrayList
        ArrayList<Number> values = new ArrayList<>();
        values.add(1);
        values.add(getPhi());
        values.add(getTheta());
        values.add(getRadius());
        return values;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        assertValidDouble(phi);
        this.phi = phi;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        assertValidDouble(theta);
        this.theta = theta;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        assertValidDouble(radius);
        assertNotNegative(radius);
        this.radius = radius;
    }

    @Override
    protected void assertClassInvariants() {
        assertValidDouble(phi);
        assertValidDouble(theta);
        assertValidDouble(radius);
        assertNotNegative(radius);
    }

    protected static void assertAngle(double angle) {
        if (angle < 0 || angle > 360) {
            throw new ArithmeticException("Angle can only be between 0 and 360 degrees.");
        }
    }
}
