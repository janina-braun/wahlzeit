package org.wahlzeit.model.coordinate;

import org.wahlzeit.model.WrongCalculationException;
import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.PatternInstance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.wahlzeit.model.AssertUtils.*;

@PatternInstance(
        patternName = "Template Method",
        participants = {"AbstractClass"}
)
public abstract class AbstractCoordinate implements Coordinate {
    final double tolerance = 0.0001;

    protected static final HashMap<Integer, AbstractCoordinate> coordinateMap = new HashMap<>();

    public abstract CartesianCoordinate asCartesianCoordinate();

    public double getCartesianDistance(Coordinate coordinate) throws WrongCalculationException {
        return this.asCartesianCoordinate().getCartesianDistance(coordinate);
    }

    public abstract SphericCoordinate asSphericCoordinate();

    public double getCentralAngle(Coordinate coordinate) throws WrongCalculationException {
        return this.asSphericCoordinate().getCentralAngle(coordinate);
    }

    @Override
    public boolean isEqual(Coordinate coordinate) {
        return this.asCartesianCoordinate().isEqual(coordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        return this.isEqual((Coordinate) o);
    }

    @Override
    public int hashCode() {
        return this.asCartesianCoordinate().hashCode();
    }

    public Coordinate readFrom(ResultSet rset) throws SQLException {
        Coordinate c = null;
        try {
            assertArgumentNotNull(rset);
            assertClassInvariants();
            int type = rset.getInt("coordinate_type");
            double c1 = rset.getDouble("coordinate1");
            double c2 = rset.getDouble("coordinate2");
            double c3 = rset.getDouble("coordinate3");
            assertValidDouble(c1);
            assertValidDouble(c2);
            assertValidDouble(c3);
            c = doReadFrom(type, c1, c2, c3);
            assertClassInvariants();
        } catch (WrongCalculationException | IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer(e.getMessage() + " Values cannot be updated.");
            SysLog.log(s);
        }
        return c;
    }

    //passes coordinate type and the values of the three coordinates to write them into the variables of the subclass
    protected abstract Coordinate doReadFrom(int type, double c1, double c2, double c3) throws WrongCalculationException;

    public void writeOn(ResultSet rset) throws SQLException {
        try {
            assertArgumentNotNull(rset);
            assertClassInvariants();
            ArrayList<Number> values = doWriteOn();
            //the ArrayList needs four entries, first an int for the coordinate type and then three doubles for the coordinates
            assertWriteOnArrayListSize(values);
            rset.updateInt("coordinate_type", (int) values.get(0));
            rset.updateDouble("coordinate1", (double) values.get(1));
            rset.updateDouble("coordinate2", (double) values.get(2));
            rset.updateDouble("coordinate3", (double) values.get(3));
            assertClassInvariants();
        } catch (IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer(e.getMessage() + " Values cannot be updated.");
            SysLog.log(s);
        }

    }

    //writes coordinate type and the values of the three coordinates into the ArrayList
    protected abstract ArrayList<Number> doWriteOn();

    protected abstract void assertClassInvariants();

    public static void assertWriteOnArrayListSize(ArrayList<Number> values) {
        if (values.size() < 4) {
            throw new IllegalArgumentException("doWriteOn must return four elements in the ArrayList.");
        }
    }
}
