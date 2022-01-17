package org.wahlzeit.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnimalManagerTest {
    AnimalManager manager = AnimalManager.getInstance();

    @Test
    public void testEnsureAnimalType() {
        AnimalType type1 = manager.ensureAnimalType("name", "animalClass", false);
        AnimalType type2 = manager.ensureAnimalType("name" , "animalClass", false);
        assertEquals(manager.animalTypes.size(), 1);
        AnimalType type3 = manager.ensureAnimalType("otherName", "otherAnimalClass", true);
        assertEquals(manager.animalTypes.size(), 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertInValidAnimalType() {
        AnimalType type = new AnimalType("newName", "newAnimalClass", false);
        manager.createAnimal(type);
    }

    @Test
    public void testCreateAnimal() {
        AnimalType type1 = manager.ensureAnimalType("name", "animalClass", false);
        AnimalType type2 = manager.ensureAnimalType("otherName", "otherAnimalClass", true);
        Animal animal1 = manager.createAnimal(type1);
        Animal animal2 = manager.createAnimal(type2, 200, "habitat");
        assertEquals(manager.animals.size(), 2);
        assertEquals(animal1.getAnimalType(), type1);
        assertEquals(animal2.getAnimalType(), type2);
        assertEquals(animal2.getWeight(), 200.0, 0.00001);
        assertEquals(animal2.getHabitat(), "habitat");
    }
}
