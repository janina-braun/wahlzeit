package org.wahlzeit.model;

import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.model.coordinate.CartesianCoordinate;
import org.wahlzeit.model.coordinate.Coordinate;

import static org.junit.Assert.*;

public class AnimalTest {
    AnimalType animalType1;
    AnimalType animalType2;
    Animal animal1;
    Animal animal2;
    Animal animal3;

    @Before
    public void setUp() {
        animalType1 = new AnimalType("elephant", "mammal", true);
        animalType2 = new AnimalType("crocodile", "reptile", true);
        animal1 = new Animal(animalType1, 4000.0, "forest");
        animal2 = new Animal(animalType2, 200.0, "water");
        animal3 = new Animal(animalType2, 200.0, "water");
    }

    @Test
    public void testIsEqual() {
        Boolean isNotEqual = animal1.isEqual(animal2);
        Boolean isEqual = animal2.isEqual(animal3);

        assertTrue(isEqual);
        assertFalse(isNotEqual);
    }

    @Test
    public void testEquals() {
        Coordinate cCoordinate = CartesianCoordinate.ensureCartesianCoordinate(1,2,3);
        Boolean notEqual = animal1.equals(animal2);
        Boolean equal = animal2.equals(animal3);
        Boolean wrongClass = animal1.equals(cCoordinate);
        Boolean nullPointer = animal1.equals(null);

        assertTrue(equal);
        assertFalse(notEqual);
        assertFalse(wrongClass);
        assertFalse(nullPointer);
    }

    @Test
    public void testHashCode() {
        int hash1 = animal1.hashCode();
        int hash2 = animal2.hashCode();
        int hash3 = animal3.hashCode();

        assertEquals(hash2, hash3);
        assertNotEquals(hash1, hash2);
    }
}
