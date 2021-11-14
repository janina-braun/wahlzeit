package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalPhotoFactory extends PhotoFactory{

    private static AnimalPhotoFactory instance = null;

    public static synchronized AnimalPhotoFactory getInstance() {
        if (instance == null) {
            SysLog.logSysInfo("setting generic AnimalPhotoFactory");
            setInstance(new AnimalPhotoFactory());
        }
        return instance;
    }

    protected static synchronized void setInstance(AnimalPhotoFactory animalPhotoFactory) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initialize PhotoFactory twice");
        }
        instance = animalPhotoFactory;
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
