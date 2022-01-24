package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;
import java.util.Objects;
import static org.wahlzeit.model.AssertUtils.*;

/**
 * Method Calls:
 * - AnimalManager calls createAnimal() with an AnimalType (and additional parameters)
 * - thereby createInstance() of AnimalType is called with the corresponding AnimalType
 * - in AnimalType calling createInstance() creates a new Animal by calling a constructor of Animal
 *
 * Object Creation Table:
 * - Delegation: separate-object (AnimalType)
 * - Selection: on-the-spot (hard coded constructor)
 * - Configuration: in-code (no Annotations/Configuration file)
 * - Instantiation: in-code (Constructor call from AnimalType)
 * - Initialization: by-fixed-signature (Animal Constructor with fixed parameter)
 * - Building: default (Animal creates dependent object)
 */

public class Animal {
    private AnimalType animalType;
    private double weight;
    private String habitat;

    public Animal(AnimalType animalType) {
        this.animalType = animalType;
        this.weight = 0.0;
        this.habitat = "";
    }

    public Animal(AnimalType animalType, double weight, String habitat) {
        this.animalType = animalType;
        try {
            assertValidDouble(weight);
            assertNotNegative(weight);
            this.weight = weight;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("Weight is not a valid double or negative. Passed weight "
                    + weight + " is set to 0.");
            SysLog.log(s);
            this.weight = 0.0;
        }
        this.habitat = habitat;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        try {
            assertNotNegative(weight);
            this.weight = weight;
        } catch (IllegalStateException e) {
            final StringBuffer s = new StringBuffer("Average weight is not a valid double or negative. average weight is not updated.");
            SysLog.log(s);
        }

    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public boolean isEqual(Animal animal) {
        try {
            assertArgumentNotNull(animal);
            return this.animalType.equals(animal.getAnimalType()) &&
                    Math.abs(this.weight - animal.getWeight()) <= 0.1 &&
                    this.habitat.equals(animal.getHabitat());
        } catch (IllegalArgumentException e) {
            final StringBuffer s = new StringBuffer("Comparison with a NullPointer not possible. Animals are not equal.");
            SysLog.log(s);
            return false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.isEqual((Animal) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalType, weight, habitat);
    }
}
