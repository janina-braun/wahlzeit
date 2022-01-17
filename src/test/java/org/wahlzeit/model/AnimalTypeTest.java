package org.wahlzeit.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnimalTypeTest {
    AnimalType superType;
    AnimalType subType;
    AnimalType subSubType;

    @Before
    public void setUp() {
        superType = new AnimalType("superType", "animalClass", false);
        subType = new AnimalType("subType", "animalClass", false);
        subSubType = new AnimalType("subSubType", "animalClass", true);
    }

    @Test
    public void testIsEqual() {
        Boolean isEqual = superType.equals(new AnimalType("superType", "animalClass", false));
        assertTrue(isEqual);
    }

    @Test
    public void testIsNotEqual() {
        Boolean isEqual = superType.equals(subType);
        assertFalse(isEqual);
    }

    @Test
    public void testAddSubType() {
        superType.addSubType(subType);
        assertEquals(subType.superType, superType);
        assertTrue(superType.hasSubType(subType));
    }

    @Test
    public void testHasInstance() {
        superType.addSubType(subType);
        Animal animal = new Animal(subType);
        assertTrue(superType.hasInstance(animal));
    }

    @Test
    public void testHasNoInstance() {
        superType.addSubType(subType);
        Animal animal= new Animal(superType);
        assertFalse(subType.hasInstance(animal));
    }

    @Test
    public void testHasSubtype() {
        superType.addSubType(subType);
        subType.addSubType(subSubType);

        assertTrue(superType.hasSubType(subType));
        assertTrue(superType.hasSubType(subSubType));
        assertTrue(subType.hasSubType(subSubType));
    }

    @Test
    public void testHasNoSubtype() {
        superType.addSubType(subType);
        subType.addSubType(subSubType);

        assertFalse(subType.hasSubType(superType));
        assertFalse(subSubType.hasSubType(superType));
        assertFalse(subSubType.hasSubType(subType));
    }

    @Test
    public void testCreateInstance() {
        superType.addSubType(subType);
        Animal created = subType.createInstance();
        Animal animal1 = new Animal(subType);
        Animal animal2 = new Animal(superType);
        assertEquals(created, animal1);
        assertNotEquals(created, animal2);
    }

    @Test
    public void testCreateInstance2() {
        superType.addSubType(subType);
        Animal created = subType.createInstance(200, "habitat");
        Animal animal1 = new Animal(subType, 200, "habitat");
        Animal animal2 = new Animal(subType, 300, "habitat");
        assertEquals(created, animal1);
        assertNotEquals(created, animal2);
    }
}
