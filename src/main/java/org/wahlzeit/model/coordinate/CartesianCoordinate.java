package org.wahlzeit.model.coordinate;

import org.wahlzeit.model.WrongCalculationException;
import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.PatternInstance;

import java.util.ArrayList;
import java.util.Objects;

import static org.wahlzeit.model.AssertUtils.*;

@PatternInstance(
        patternName = "Template Method",
        participants = {"ConcreteClass"}
)
public class CartesianCoordinate extends AbstractCoordinate {
    private final double x;
    private final double y;
    private final double z;

    CartesianCoordinate(double x, double y, double z) {
        double tmp_x, tmp_y, tmp_z;
        try {
            assertValidDouble(x);
            assertValidDouble(y);
            assertValidDouble(z);
            tmp_x = x;
            tmp_y = y;
            tmp_z = z;
        } catch (IllegalStateException e) {
            //If one coordinate is not valid and set to 0.0, the other coordinates won't fit anymore and are also set to 0.0.
            final StringBuffer s = new StringBuffer("One value is not a valid double. x, y and z are set to 0.0.");
            SysLog.log(s);
            tmp_x = 0.0;
            tmp_y = 0.0;
            tmp_z = 0.0;
        }
        this.x = tmp_x;
        this.y = tmp_y;
        this.z = tmp_z;
        assertClassInvariants();
    }

    //returns CartesianCoordinate from coodinateMap if exits, or creates and stores new CartesianCoordinate
    public static CartesianCoordinate ensureCartesianCoordinate(double x, double y, double z) {
        CartesianCoordinate c = new CartesianCoordinate(x, y, z);
        int id = c.hashCode();
        AbstractCoordinate result = coordinateMap.get(id);
        if (result == null) {
            synchronized (coordinateMap) {
                coordinateMap.put(id, c);
            }
            return c;
        } else {
            return result.asCartesianCoordinate();
        }
    }

    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public double getCartesianDistance(Coordinate coordinate) throws WrongCalculationException {
        try {
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
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new WrongCalculationException(e.getMessage());
        } catch (ArithmeticException e) {
            throw new WrongCalculationException("Arithmetic error in calculation of getCartesianDistance.");
        }

    }

    public SphericCoordinate asSphericCoordinate() {
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
        assertClassInvariants();
        return spheric;
    }

    @Override
    public boolean isEqual(Coordinate coordinate) {
        try {
            assertArgumentNotNull(coordinate);
            assertClassInvariants();
            CartesianCoordinate c;
            c = coordinate.asCartesianCoordinate();
            boolean equal = false;
            if (Math.abs(x - c.getX()) <= tolerance) {
                if (Math.abs(y - c.getY()) <= tolerance) {
                    equal = Math.abs(z - c.getZ()) <= tolerance;
                }
            }
            assertClassInvariants();
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
    public Coordinate doReadFrom(int type, double c1, double c2, double c3) {

        if (type == 0) { //type cartesian
            //coordinates of the ResultSet are of type cartesian
            return CartesianCoordinate.ensureCartesianCoordinate(c1, c2, c3);
        } else { //type spheric
            //Coordinates of the ResultSet are of type spheric and have to be converted before saving
            CartesianCoordinate c = SphericCoordinate.ensureSphericCoordinate(c1, c2, c3).asCartesianCoordinate();
            return CartesianCoordinate.ensureCartesianCoordinate(c.getX(), c.getY(), c.getZ());
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

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    protected void assertClassInvariants() {
        assertValidDouble(x);
        assertValidDouble(y);
        assertValidDouble(z);
    }
}
