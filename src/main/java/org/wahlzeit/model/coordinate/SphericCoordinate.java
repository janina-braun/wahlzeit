package org.wahlzeit.model.coordinate;

import org.wahlzeit.model.WrongCalculationException;
import org.wahlzeit.services.SysLog;

import java.util.ArrayList;

import static org.wahlzeit.model.AssertUtils.*;

public class SphericCoordinate extends AbstractCoordinate {
    private final double phi;
    private final double theta;
    private final double radius;

     SphericCoordinate(double phi, double theta, double radius) {
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
            tmp_phi = 0.0;
            tmp_theta = 0.0;
            tmp_radius = 0.0;
        }
        this.phi = tmp_phi;
        this.theta = tmp_theta;
        this.radius = tmp_radius;
        assertClassInvariants();
    }

    //returns CartesianCoordinate from coodinateMap if exits, or creates and stores new SphericCoordinate
    public static SphericCoordinate ensureSphericCoordinate(double phi, double theta, double radius) {
        CartesianCoordinate c = new SphericCoordinate(phi, theta, radius).asCartesianCoordinate();
        int id = c.hashCode();
        AbstractCoordinate result = coordinateMap.get(id);
        if (result == null) {
            synchronized (coordinateMap) {
                coordinateMap.put(id, c);
            }
            return c.asSphericCoordinate();
        } else {
            return result.asSphericCoordinate();
        }
    }

    public CartesianCoordinate asCartesianCoordinate() {
        assertClassInvariants();
        double x = radius * Math.cos(phi) * Math.sin(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(theta);
        CartesianCoordinate cartesian = new CartesianCoordinate(x, y, z);
        assertClassInvariants();
        return cartesian;
    }

    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCentralAngle(Coordinate coordinate) throws WrongCalculationException {
        try {
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
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new WrongCalculationException(e.getMessage());
        } catch (ArithmeticException e) {
            throw new WrongCalculationException("Arithmetic error in calculation of getCentralAngle.");
        }

    }

    //get coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public Coordinate doReadFrom(int type, double c1, double c2, double c3) {
        if (type == 0) { //type cartesian
            //Coordinates of the ResultSet are of type cartesian and have to be converted before saving
            SphericCoordinate s;
            s = CartesianCoordinate.ensureCartesianCoordinate(c1, c2, c3).asSphericCoordinate();
            return SphericCoordinate.ensureSphericCoordinate(s.getPhi(), s.getTheta(), s.getRadius());
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric
            return SphericCoordinate.ensureSphericCoordinate(c1, c2, c3);
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

    public double getTheta() {
        return theta;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    protected void assertClassInvariants() {
        assertValidDouble(phi);
        assertValidDouble(theta);
        assertValidDouble(radius);
        assertNotNegative(radius);
    }

    public static void assertAngle(double angle) {
        if (angle < 0 || angle > 360) {
            throw new ArithmeticException("Angle can only be between 0 and 360 degrees.");
        }
    }
}
