package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.util.ArrayList;

import static org.wahlzeit.model.AssertUtils.*;

public class SphericCoordinate extends AbstractCoordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) {
        double tmp_phi, tmp_theta, tmp_radius;
        try {
            assertValidDouble(phi);
            assertValidDouble(theta);
            assertValidDouble(radius);
            assertNotNegative(radius);
            tmp_phi = phi;
            tmp_theta = theta;
            tmp_radius = radius;
        } catch (IllegalStateException e) {
            //If one coordinate is not valid and set to 0.0, the other coordinates won't fit anymore and are also set to 0.0.
            final StringBuffer s = new StringBuffer("One value is not a valid double or radius is negative. radius, phi and theta are set to 0.0.");
            SysLog.log(s);
            tmp_phi = 0.0;
            tmp_theta = 0.0;
            tmp_radius = 0.0;
        }
        if (radius <= tolerance) {
            //if radius = 0.0, theta and phi are also 0.0
            setPhi(0.0);
            setTheta(0.0);
            setRadius(0.0);
        } else {
            setPhi(tmp_phi);
            setTheta(tmp_theta);
            setRadius(tmp_radius);
        }
        assertClassInvariants();
    }

    public CartesianCoordinate asCartesianCoordinate() throws WrongCalculationException {
        try {
            assertClassInvariants();
            double backup_phi = phi;
            double backup_theta = theta;
            double backup_radius = radius;
            double x = radius * Math.cos(phi) * Math.sin(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(theta);
            CartesianCoordinate cartesian = new CartesianCoordinate(x, y, z);
            //CartesianCoordinate Constructor executes assertClassInvariants for cartesian
            try {
                assertClassInvariants();
            } catch (IllegalStateException e) {
                //If the class invariants were corrupted during the calculation, reset to value before calculation.
                phi = backup_phi;
                theta = backup_theta;
                radius = backup_radius;
            }
            return cartesian;
        } catch (ArithmeticException e) {
            throw new WrongCalculationException("Arithmetic error in calculation of asCartesianCoordinate.");
        }
    }

    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCentralAngle(Coordinate coordinate) throws WrongCalculationException {
        try {
            assertArgumentNotNull(coordinate);
            assertClassInvariants();
            double backup_phi = phi;
            double backup_theta = theta;
            double backup_radius = radius;
            SphericCoordinate s = coordinate.asSphericCoordinate();
            double part1 = Math.sin(phi) * Math.sin(s.getPhi());
            double deltaTheta = Math.abs(s.getTheta() - theta);
            double part2 = Math.cos(phi) * Math.cos(s.getPhi()) * Math.cos(deltaTheta);
            double centralAngle = Math.acos(part1 + part2);
            assertValidDouble(centralAngle);
            assertAngle(centralAngle);
            try {
                assertClassInvariants();
            } catch (IllegalStateException e) {
                //If the class invariants were corrupted during the calculation, reset to value before calculation.
                phi = backup_phi;
                theta = backup_theta;
                radius = backup_radius;
            }
            return centralAngle;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new WrongCalculationException(e.getMessage());
        } catch (ArithmeticException e) {
            throw new WrongCalculationException("Arithmetic error in calculation of getCentralAngle.");
        }

    }

    //get coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public void doReadFrom(int type, double c1, double c2, double c3) throws WrongCalculationException {
        if (type == 0) { //type cartesian
            //Coordinates of the ResultSet are of type cartesian and have to be converted before saving
            SphericCoordinate s;
            try {
                s = new CartesianCoordinate(c1, c2, c3).asSphericCoordinate();
            } catch (WrongCalculationException e) {
                throw new WrongCalculationException("Arithmetic error in calculation of asSphericCoordinate.");
            }

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
        try {
            assertValidDouble(phi);
            this.phi = phi;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("phi is not a valid double. " + e.getMessage() + " phi is not updated.");
            SysLog.log(s);
        }
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        try {
            assertValidDouble(theta);
            this.theta = theta;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("theta is not a valid double. " + e.getMessage() + " theta is not updated.");
            SysLog.log(s);
        }
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        try {
            assertValidDouble(radius);
            assertNotNegative(radius);
            this.radius = radius;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("radius: " + e.getMessage() + " radius is not updated.");
            SysLog.log(s);
        }
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
