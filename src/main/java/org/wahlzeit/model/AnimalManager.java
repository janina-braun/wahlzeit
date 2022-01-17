package org.wahlzeit.model;

import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AnimalManager extends ObjectManager {
    //Singleton
    protected static final AnimalManager instance = new AnimalManager();

    protected HashMap<Integer, AnimalType> animalTypes = new HashMap<Integer, AnimalType>();
    protected HashMap<Integer, Animal> animals = new HashMap<Integer, Animal>();

    public AnimalType ensureAnimalType(String name, String animalClass, boolean vegetarian) {
        AnimalType animalType = new AnimalType(name, animalClass, vegetarian);
        int index = animalType.hashCode();
        AnimalType exitingType = animalTypes.get(index);
        if (exitingType != null) {
            return exitingType;
        }
        animalTypes.put(index, animalType);
        return animalType;
    }

    public Animal createAnimal(AnimalType animalType) {
        assertIsValidType(animalType);
        Animal animal = animalType.createInstance();
        animals.put(animal.hashCode(), animal);
        return animal;
    }

    public Animal createAnimal(AnimalType animalType, double weight, String habitat) {
        assertIsValidType(animalType);
        Animal animal = animalType.createInstance(weight, habitat);
        animals.put(animal.hashCode(), animal);
        return animal;
    }

    private void assertIsValidType(AnimalType animalType) {
        if(!animalTypes.containsKey(animalType.hashCode())) {
            throw new IllegalArgumentException("Animal type does not exist");
        }
    }

    public static final AnimalManager getInstance() {
        return  instance;
    }

    @Override
    protected Persistent createObject(ResultSet rset) throws SQLException {
        return null;
    }
}
