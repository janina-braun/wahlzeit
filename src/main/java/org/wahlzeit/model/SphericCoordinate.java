package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SphericCoordinate extends DataObject implements Coordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) {
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        double x = radius * Math.cos(phi) * Math.sin(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(theta);
        return new CartesianCoordinate(x, y, z);
    }

    @Override
    public double getCartesianDistance(Coordinate coordinate) {
        return this.asCartesianCoordinate().getCartesianDistance(coordinate);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCentralAngle(Coordinate coordinate) {
        SphericCoordinate s = coordinate.asSphericCoordinate();
        double part1 = Math.sin(phi) * Math.sin(s.getPhi());
        double deltaTheta = Math.abs(s.getTheta() - theta);
        double part2 = Math.cos(phi) * Math.cos(s.getPhi()) * Math.cos(deltaTheta);
        return Math.acos(part1 + part2);
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
        return Objects.hash(phi, theta, radius);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("coordinate_type", 1);
        rset.updateDouble("coordinate1", getPhi());
        rset.updateDouble("coordinate2", getTheta());
        rset.updateDouble("coordinate3", getRadius());
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        if (rset.getInt("coordinate_type") == 0) { //type cartesian
            double x = rset.getDouble("coordinate1");
            double y = rset.getDouble("coordinate2");
            double z = rset.getDouble("coordinate3");
            SphericCoordinate s = new CartesianCoordinate(x, y, z).asSphericCoordinate();
            setPhi(s.getPhi());
            setTheta(s.getTheta());
            setRadius(s.getRadius());
        } else { //type spheric
            setPhi(rset.getDouble("coordinate1"));
            setTheta(rset.getDouble("coordinate2"));
            setRadius(rset.getDouble("coordinate3"));
        }
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

    @Override
    public String getIdAsString() {
        return null;
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {

    }
}
