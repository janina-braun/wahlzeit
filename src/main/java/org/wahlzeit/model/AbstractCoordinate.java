package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class AbstractCoordinate extends DataObject implements Coordinate {
    final double tolerance = 0.0001;

    public abstract CartesianCoordinate asCartesianCoordinate();

    public double getCartesianDistance(Coordinate coordinate) {
        return this.asCartesianCoordinate().getCartesianDistance(coordinate);
    }

    public abstract SphericCoordinate asSphericCoordinate();

    public double getCentralAngle(Coordinate coordinate) {
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

    public void readFrom(ResultSet rset) throws SQLException {
        int type = rset.getInt("coordinate_type");
        double c1 = rset.getDouble("coordinate1");
        double c2 = rset.getDouble("coordinate2");
        double c3 = rset.getDouble("coordinate3");
        doReadFrom(type, c1, c2, c3);
    }

    //passes coordinate type and the values of the three coordinates to write them into the variables of the subclass
    public abstract void doReadFrom(int type, double c1, double c2, double c3);

    public void writeOn(ResultSet rset) throws SQLException {
        ArrayList<Number> values = doWriteOn();
        //the ArrayList needs four entries, first an int for the coordinate type and then three doubles for the coordinates
        if (values.size() < 4) {
            throw new IllegalArgumentException("doWriteOn must return four elements in the ArrayList.");
        }
        rset.updateInt("coordinate_type", (int) values.get(0));
        rset.updateDouble("coordinate1", (double) values.get(1));
        rset.updateDouble("coordinate2", (double) values.get(2));
        rset.updateDouble("coordinate3", (double) values.get(3));
    }

    //writes coordinate type and the values of the three coordinates into the ArrayList
    public abstract ArrayList<Number> doWriteOn();

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {

    }

    @Override
    public String getIdAsString() {
        return null;
    }
}
