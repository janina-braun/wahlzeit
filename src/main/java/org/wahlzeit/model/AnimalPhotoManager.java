package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalPhotoManager extends PhotoManager{

    private static boolean isInitialized = false;

    public static synchronized AnimalPhotoManager getInstance() {
        if (!isInitialized) {
            SysLog.logSysInfo("setting specialized AnimalPhotoManager");
            PhotoManager.setInstance(new AnimalPhotoManager());
            isInitialized = true;
        }
        return (AnimalPhotoManager) PhotoManager.getInstance();
    }

    public static void initialize() {
        getInstance();
    }

    public AnimalPhotoManager() {
        photoTagCollector = AnimalPhotoFactory.getInstance().createPhotoTagCollector();
    }

    @Override
    protected Photo createObject(ResultSet rset) throws SQLException {
        return AnimalPhotoFactory.getInstance().createPhoto(rset);
    }
}
