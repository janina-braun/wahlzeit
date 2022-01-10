package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.PatternInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

@PatternInstance(
        patternName = "Abstract Factory",
        participants = {"ConcreteFactory"}
)
public class AnimalPhotoFactory extends PhotoFactory{

    private static boolean isInitialized = false;

    public static synchronized AnimalPhotoFactory getInstance() {
        if (!isInitialized) {
            SysLog.logSysInfo("setting specialized AnimalPhotoFactory");
            PhotoFactory.setInstance(new AnimalPhotoFactory());
            isInitialized = true;
        }
        return (AnimalPhotoFactory) PhotoFactory.getInstance();
    }

    protected AnimalPhotoFactory() {
        //do nothing
    }

    @Override
    public Photo createPhoto() {
        return new AnimalPhoto();
    }

    @Override
    public Photo createPhoto(PhotoId id) {
        return new AnimalPhoto(id);
    }

    public Photo createPhoto(Animal animal) {
        return new AnimalPhoto(animal);
    }

    public Photo createPhoto(PhotoId id, Animal animal) {
        return new AnimalPhoto(id, animal);
    }

    @Override
    public Photo createPhoto(ResultSet rs) throws SQLException {
        return new AnimalPhoto(rs);
    }
}
