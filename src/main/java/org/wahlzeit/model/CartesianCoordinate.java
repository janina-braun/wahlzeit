package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.util.ArrayList;
import java.util.Objects;

import static org.wahlzeit.model.AssertUtils.*;

public class CartesianCoordinate extends AbstractCoordinate {
    private double x;
    private double y;
    private double z;

    public CartesianCoordinate(double x, double y, double z) {
        try {
            assertValidDouble(x);
            assertValidDouble(y);
            assertValidDouble(z);
            setX(x);
            setY(y);
            setZ(z);
        } catch (IllegalStateException e) {
            //If one coordinate is not valid and set to 0.0, the other coordinates won't fit anymore and are also set to 0.0.
            final StringBuffer s = new StringBuffer("One value is not a valid double. x, y and z are set to 0.0.");
            SysLog.log(s);
            setX(0.0);
            setY(0.0);
            setZ(0.0);
        }
        assertClassInvariants();
    }

    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public double getCartesianDistance(Coordinate coordinate) throws WrongCalculationException {
        try {
            assertArgumentNotNull(coordinate);
            assertClassInvariants();
            double backup_x = x;
            double backup_y = y;
            double backup_z = z;
            CartesianCoordinate c = coordinate.asCartesianCoordinate();
            double d_x = Math.pow(x - c.getX(), 2); //(x1 - x2)^2
            double d_y = Math.pow(y - c.getY(), 2); //(y1 - y2)^2
            double d_z = Math.pow(z - c.getZ(), 2); //(z1 - z2)^2
            double distance = Math.sqrt(d_x + d_y + d_z);
            assertValidDouble(distance);
            assertNotNegative(distance);
            try {
                assertClassInvariants();
            } catch (IllegalStateException e) {
                //If the class invariants were corrupted during the calculation, reset to value before calculation.
                x = backup_x;
                y = backup_y;
                z = backup_z;
            }
            return distance;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new WrongCalculationException(e.getMessage());
        } catch (ArithmeticException e) {
            throw new WrongCalculationException("Arithmetic error in calculation of getCartesianDistance.");
        }

    }

    public SphericCoordinate asSphericCoordinate() throws WrongCalculationException {
        try {
            assertClassInvariants();
            double backup_x = x;
            double backup_y = y;
            double backup_z = z;
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
            try {
                assertClassInvariants();
            } catch (IllegalStateException e) {
                //If the class invariants were corrupted during the calculation, reset to value before calculation.
                x = backup_x;
                y = backup_y;
                z = backup_z;
            }
            return spheric;
        } catch (ArithmeticException e) {
            throw new WrongCalculationException("Arithmetic error in calculation of asSphericCoordinate.");
        }
    }

    @Override
    public boolean isEqual(Coordinate coordinate) throws WrongCalculationException {
        try {
            assertArgumentNotNull(coordinate);
            assertClassInvariants();
            double backup_x = x;
            double backup_y = y;
            double backup_z = z;
            CartesianCoordinate c;
            try {
                 c = coordinate.asCartesianCoordinate();
            } catch (WrongCalculationException e) {
                throw new WrongCalculationException("Arithmetic error in calculation of asCartesianCoordinate of argument coodinate.");
            }
            boolean equal = false;
            if (Math.abs(x - c.getX()) <= tolerance) {
                if (Math.abs(y - c.getY()) <= tolerance) {
                    equal = Math.abs(z - c.getZ()) <= tolerance;
                }
            }
            try {
                assertClassInvariants();
            } catch (IllegalStateException e) {
                //If the class invariants were corrupted during the calculation, reset to value before calculation.
                x = backup_x;
                y = backup_y;
                z = backup_z;
            }
            return equal;
        } catch (IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer("Comparison with a NullPointer not possible. Coordinates are not equal.");
            SysLog.log(s);
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    //get coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public void doReadFrom(int type, double c1, double c2, double c3) throws WrongCalculationException {
        if (type == 0) { //type cartesian
            //coordinates of the ResultSet are of type cartesian
            setX(c1);
            setY(c2);
            setZ(c3);
            //setter execute assertValidDouble
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric and have to be converted before saving
            CartesianCoordinate c;
            try {
                c = new SphericCoordinate(c1, c2, c3).asCartesianCoordinate();
            } catch (WrongCalculationException e) {
                throw new WrongCalculationException("Arithmetic error in calculation of asCartesianCoordinate.");
            }
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
        try {
            assertValidDouble(x);
            this.x = x;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("x is not a valid double. " + e.getMessage() + " x is not updated.");
            SysLog.log(s);
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        try {
            assertValidDouble(y);
            this.y = y;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("y is not a valid double. " + e.getMessage() + " y is not updated.");
            SysLog.log(s);
        }
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        try {
            assertValidDouble(z);
            this.z = z;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("z is not a valid double. " + e.getMessage() + " z is not updated.");
            SysLog.log(s);
        }
    }

    @Override
    protected void assertClassInvariants() {
        assertValidDouble(x);
        assertValidDouble(y);
        assertValidDouble(z);
    }
}
