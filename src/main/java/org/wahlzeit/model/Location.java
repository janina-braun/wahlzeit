package org.wahlzeit.model;

import org.wahlzeit.model.coordinate.CartesianCoordinate;
import org.wahlzeit.model.coordinate.Coordinate;
import org.wahlzeit.services.DataObject;
import org.wahlzeit.services.SysLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.wahlzeit.model.AssertUtils.assertArgumentNotNull;

public class Location extends DataObject {

    protected Coordinate coordinate;

    public Location(Coordinate coordinate) {
        try {
            assertArgumentNotNull(coordinate);
            this.coordinate = coordinate;
        } catch (IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer("Coordinate is NullPointer. Fall back to default cartesian coordinate.");
            SysLog.log(s);
            this.coordinate = CartesianCoordinate.ensureCartesianCoordinate(0, 0, 0);
        }
    }


    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        //readFrom returns new Coordinate object instead of changing the state of the object
        coordinate = coordinate.readFrom(rset);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        coordinate.writeOn(rset);
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {

    }

    @Override
    public String getIdAsString() {
        return null;
    }
}
