package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnimalPhotoFactoryTest {
    @Test
    public void testIsEqual() {
        AnimalType animalType = new AnimalType("elephant", "mammal", true);
        Animal animal = new Animal(animalType, 4000.0, "forest");
        PhotoId id = new PhotoId(1);
        AnimalPhotoFactory apf = new AnimalPhotoFactory();
        AnimalPhoto aPhoto = (AnimalPhoto) apf.createPhoto(id, animal);

        assertEquals(aPhoto.getId(), id);
        assertEquals(aPhoto.getAnimal().getAnimalType().getName(), "elephant");
        assertEquals(aPhoto.getAnimal().getAnimalType().getAnimalClass(), "mammal");
        assertEquals(aPhoto.getAnimal().getWeight(), 4000.0, 0.000001);
        assertTrue(aPhoto.getAnimal().getAnimalType().isVegetarian());
        assertEquals(aPhoto.getAnimal().getHabitat(), "forest");
    }
}
