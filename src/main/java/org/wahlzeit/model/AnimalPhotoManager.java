package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalPhotoManager extends PhotoManager{

    protected static final AnimalPhotoManager instance = new AnimalPhotoManager();

    public AnimalPhotoManager() {
        photoTagCollector = AnimalPhotoFactory.getInstance().createPhotoTagCollector();
    }

    @Override
    protected Photo createObject(ResultSet rset) throws SQLException {
        return AnimalPhotoFactory.getInstance().createPhoto(rset);
    }
}
