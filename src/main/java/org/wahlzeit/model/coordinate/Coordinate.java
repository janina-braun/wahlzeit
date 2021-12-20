package org.wahlzeit.model.coordinate;

import org.wahlzeit.model.WrongCalculationException;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Coordinate {
    public CartesianCoordinate asCartesianCoordinate();
    public double getCartesianDistance(Coordinate coordinate) throws WrongCalculationException;
    public SphericCoordinate asSphericCoordinate();
    public double getCentralAngle(Coordinate coordinate) throws WrongCalculationException;
    public boolean isEqual(Coordinate coordinate);
    public Coordinate readFrom(ResultSet rset) throws SQLException;
    public void writeOn(ResultSet rset) throws SQLException;
}
