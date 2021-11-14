package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnimalPhotoFactoryTest {
    @Test
    public void testIsEqual() {
        Animal animal = new Animal("elephant", "mammal", 4000.0, true, "forest");
        PhotoId id = new PhotoId(1);
        AnimalPhotoFactory apf = new AnimalPhotoFactory();
        AnimalPhoto aPhoto = (AnimalPhoto) apf.createPhoto(id, animal);

        assertEquals(aPhoto.getId(), id);
        assertEquals(aPhoto.getAnimal().getName(), "elephant");
        assertEquals(aPhoto.getAnimal().getAnimalClass(), "mammal");
        assertEquals(aPhoto.getAnimal().getAvg_weight(), 4000.0, 0.000001);
        assertTrue(aPhoto.getAnimal().isVegetarian());
        assertEquals(aPhoto.getAnimal().getHabitat(), "forest");
    }
}
