package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Coordinate {
    public CartesianCoordinate asCartesianCoordinate() throws WrongCalculationException;
    public double getCartesianDistance(Coordinate coordinate) throws WrongCalculationException;
    public SphericCoordinate asSphericCoordinate() throws WrongCalculationException;
    public double getCentralAngle(Coordinate coordinate) throws WrongCalculationException;
    public boolean isEqual(Coordinate coordinate) throws WrongCalculationException;
    public void readFrom(ResultSet rset) throws SQLException;
    public void writeOn(ResultSet rset) throws SQLException;
}
