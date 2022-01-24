package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.PatternInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.wahlzeit.model.AssertUtils.assertArgumentNotNull;

/**
 * Method Calls:
 * - AnimalPhotoManager calls createObject() with a ResultSet
 * - thereby createPhoto() of AnimalPhotoFactory is called with the ResultSet
 * - in AnimalPhotoFactory calling createPhoto() creates a new photo by calling a constructor of AnimalPhoto with the ResultSet
 *
 * Object Creation Table:
 * - Delegation: separate-object (AnimalPhotoFactory)
 * - Selection: by-subclassing (AnimalPhoto extends Photo)
 * - Configuration: in-code (no Annotations/Configuration file)
 * - Instantiation: in-code (Constructor call from AnimalPhotoFactory)
 * - Initialization: by-fixed-signature (AnimalPhoto Constructor with fixed parameter)
 * - Building: default (AnimalPhoto creates dependent object)
 */

@PatternInstance(
        patternName = "Abstract Factory",
        participants = {"ConcreteProduct"}
)
public class AnimalPhoto extends Photo{
    private Animal animal = null;
    private static AnimalManager animalManager = AnimalManager.getInstance();

    public AnimalPhoto() {
        super();
    }

    public AnimalPhoto(PhotoId myId) {
        super(myId);
    }

    public AnimalPhoto(Animal animal) {
        super();
        this.animal = animal;
    }

    public AnimalPhoto(PhotoId myId, Animal animal) {
        super(myId);
        this.animal = animal;
    }

    public AnimalPhoto(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        try {
            assertArgumentNotNull(rset);
            super.readFrom(rset);
            String name = rset.getString("animal_name");
            String animalClass = rset.getString("animal_class");
            double weight = rset.getDouble("weight");
            Boolean vegetarian = rset.getBoolean("vegetarian");
            String habitat = rset.getString("habitat");
            AnimalType animalType = animalManager.ensureAnimalType(name, animalClass, vegetarian);
            if (animal != null && animal.getAnimalType().equals(animalType) ) {
                animal.setWeight(weight);
                animal.setHabitat(habitat);
            } else {
                animal = animalManager.createAnimal(animalType, weight, habitat);
            }
        } catch (IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer("ResultSet is NullPointer. Values cannot be updated.");
            SysLog.log(s);
        }

    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        try {
            assertArgumentNotNull(rset);
            super.writeOn(rset);
            if (animal != null) {
                rset.updateString("animal_name", animal.getAnimalType().getName());
                rset.updateString("animal_class", animal.getAnimalType().getAnimalClass());
                rset.updateDouble("avg_weight", animal.getWeight());
                rset.updateBoolean("vegetarian", animal.getAnimalType().isVegetarian());
                rset.updateString("habitat", animal.getHabitat());
            }
        } catch (IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer("ResultSet is NullPointer. Values cannot be updated.");
            SysLog.log(s);
        }
    }
}
