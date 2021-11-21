package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CartesianCoordinate extends DataObject implements Coordinate{
    final double tolerance = 0.0001;
    private double x;
    private double y;
    private double z;

    public CartesianCoordinate(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public double getCartesianDistance(Coordinate coordinates) {
        CartesianCoordinate c = coordinates.asCartesianCoordinate();
        double d_x = Math.pow(x - c.getX(), 2); //(x1 - x2)^2
        double d_y = Math.pow(y - c.getY(), 2); //(y1 - y2)^2
        double d_z = Math.pow(z - c.getZ(), 2); //(z1 - z2)^2
        return Math.sqrt(d_x + d_y + d_z);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        double theta = Math.acos(z / radius);
        double phi = Math.atan2(y, x);
        return new SphericCoordinate(phi, theta, radius);
    }

    @Override
    public double getCentralAngle(Coordinate coordinate) {
        return this.asSphericCoordinate().getCentralAngle(coordinate);
    }

    public boolean isEqual(Coordinate coordinate) {
        CartesianCoordinate c = coordinate.asCartesianCoordinate();
        if (Math.abs(this.x - c.getX()) <= tolerance) {
            if (Math.abs(this.y - c.getY()) <= tolerance) {
                return Math.abs(this.z - c.getZ()) <= tolerance;
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

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        if (rset.getInt("coordinate_type") == 0) { //type cartesian
            setX(rset.getDouble("coordinate1"));
            setY(rset.getDouble("coordinate2"));
            setZ(rset.getDouble("coordinate3"));
        } else { //type spheric
            double phi = rset.getDouble("coordinate1");
            double theta = rset.getDouble("coordinate2");
            double radius = rset.getDouble("coordinate3");
            CartesianCoordinate c = new SphericCoordinate(phi, theta, radius).asCartesianCoordinate();
            setX(c.getX());
            setY(c.getY());
            setZ(c.getZ());
        }
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("coordinate_type", 0);
        rset.updateDouble("coordinate1", getX());
        rset.updateDouble("coordinate2", getY());
        rset.updateDouble("coordinate3", getZ());
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

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {

    }

    @Override
    public String getIdAsString() {
        return null;
    }
}
