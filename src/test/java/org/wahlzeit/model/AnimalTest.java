package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class AnimalTest {
    @Test
    public void testIsEqual() {
        Animal animal1 = new Animal("elephant", "mammal", 4000.0, true, "forest");
        Animal animal2 = new Animal("crocodile", "reptile", 200.0, true, "water");
        Animal animal3 = new Animal("crocodile", "reptile", 200.0, true, "water");
        Boolean isNotEqual = animal1.isEqual(animal2);
        Boolean isEqual = animal2.isEqual(animal3);

        assertTrue(isEqual);
        assertFalse(isNotEqual);
    }

    @Test
    public void testEquals() {
        Coordinate cCoordinate = new CartesianCoordinate(1,2,3);
        Animal animal1 = new Animal("elephant", "mammal", 4000.0, true, "forest");
        Animal animal2 = new Animal("crocodile", "reptile", 200.0, true, "water");
        Animal animal3 = new Animal("crocodile", "reptile", 200.0, true, "water");
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
        Animal animal1 = new Animal("elephant", "mammal", 4000.0, true, "forest");
        Animal animal2 = new Animal("crocodile", "reptile", 200.0, true, "water");
        Animal animal3 = new Animal("crocodile", "reptile", 200.0, true, "water");

        int hash1 = animal1.hashCode();
        int hash2 = animal2.hashCode();
        int hash3 = animal3.hashCode();

        assertEquals(hash2, hash3);
        assertNotEquals(hash1, hash2);
    }
}
