package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Coordinate extends DataObject {
    private double x;
    private double y;
    private double z;

    public Coordinate(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
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

    public double getDistance(Coordinate coordinates) {
        double d_x = Math.pow(this.x - coordinates.getX(), 2); //(x1 - x2)^2
        double d_y = Math.pow(this.y - coordinates.getY(), 2); //(y1 - y2)^2
        double d_z = Math.pow(this.z - coordinates.getZ(), 2); //(z1 - z2)^2

        return Math.sqrt(d_x + d_y + d_z);
    }

    public boolean isEqual(Coordinate coordinate) {
        if (Math.abs(this.x - coordinate.getX()) <= 0.1) {
            if (Math.abs(this.y - coordinate.getY()) <= 0.1) {
                return Math.abs(this.z - coordinate.getZ()) <= 0.1;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.isEqual((Coordinate) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        setX(rset.getDouble("x_coordinate"));
        setY(rset.getDouble("y_coordinate"));
        setZ(rset.getDouble("z_coordinate"));
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateDouble("x_coordinate", getX());
        rset.updateDouble("y_coordinate", getY());
        rset.updateDouble("z_coordinate", getZ());
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {

    }

    @Override
    public String getIdAsString() {
        return null;
    }
}
