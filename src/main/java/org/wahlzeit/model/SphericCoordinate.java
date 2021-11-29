package org.wahlzeit.model;

import java.util.ArrayList;
import java.util.Objects;

public class SphericCoordinate extends AbstractCoordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) throws IllegalArgumentException{
        if (radius < 0) {
            //negative radius is a illegal argument
            //radius must be >= 0.0
            throw new IllegalArgumentException("Radius cannot be negative.");
        } else if (radius <= tolerance) {
            //if radius = 0.0, theta and phi are also 0.0
            this.phi = 0.0;
            this.theta = 0.0;
            this.radius = 0.0;
        } else {
            this.phi = phi;
            this.theta = theta;
            this.radius = radius;
        }
    }

    public CartesianCoordinate asCartesianCoordinate() throws ArithmeticException {
        double x = radius * Math.cos(phi) * Math.sin(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(theta);
        return new CartesianCoordinate(x, y, z);
    }

    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phi, theta, radius);
    }

    //get coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public void doReadFrom(int type, double c1, double c2, double c3) {
        if (type == 0) { //type cartesian
            //Coordinates of the ResultSet are of type catesian and have to be converted before saving
            SphericCoordinate s = new CartesianCoordinate(c1, c2, c3).asSphericCoordinate();
            setPhi(s.getPhi());
            setTheta(s.getTheta());
            setRadius(s.getRadius());
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric
            setPhi(c1);
            setTheta(c2);
            setRadius(c3);
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
        this.phi = phi;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
