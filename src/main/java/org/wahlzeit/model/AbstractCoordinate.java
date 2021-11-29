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
        CartesianCoordinate c1 = this.asCartesianCoordinate();
        CartesianCoordinate c2 = coordinate.asCartesianCoordinate();
        double d_x = Math.pow(c1.getX() - c2.getX(), 2); //(x1 - x2)^2
        double d_y = Math.pow(c1.getY() - c2.getY(), 2); //(y1 - y2)^2
        double d_z = Math.pow(c1.getZ() - c2.getZ(), 2); //(z1 - z2)^2
        return Math.sqrt(d_x + d_y + d_z);
    }

    public abstract SphericCoordinate asSphericCoordinate();

    public double getCentralAngle(Coordinate coordinate) {
        SphericCoordinate s1 = this.asSphericCoordinate();
        SphericCoordinate s2 = coordinate.asSphericCoordinate();
        double part1 = Math.sin(s1.getPhi()) * Math.sin(s2.getPhi());
        double deltaTheta = Math.abs(s2.getTheta() - s1.getTheta());
        double part2 = Math.cos(s1.getPhi()) * Math.cos(s2.getPhi()) * Math.cos(deltaTheta);
        return Math.acos(part1 + part2);
    }

    @Override
    public boolean isEqual(Coordinate coordinate) {
        CartesianCoordinate c1 = this.asCartesianCoordinate();
        CartesianCoordinate c2 = coordinate.asCartesianCoordinate();
        if (Math.abs(c1.getX() - c2.getX()) <= tolerance) {
            if (Math.abs(c1.getY() - c2.getY()) <= tolerance) {
                return Math.abs(c1.getZ() - c2.getZ()) <= tolerance;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        return this.isEqual((Coordinate) o);
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
