package org.wahlzeit.model;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class AnimalType {
    //type specifics
    private String name;
    private String animalClass;
    private boolean vegetarian;

    protected AnimalType superType = null;
    protected Set<AnimalType> subTypes = new HashSet<AnimalType>();

    public AnimalType(String name, String animalClass, boolean vegetarian) {
        this.name = name;
        this.animalClass = animalClass;
        this.vegetarian = vegetarian;
    }

    public Animal createInstance() {
        return  new Animal(this);
    }

    public Animal createInstance(double weight, String habitat) {
        return new Animal(this, weight, habitat);
    }

    public boolean hasInstance(Animal animal) {
        assert (animal != null): "Animal is null";
        if (animal.getAnimalType() == this) {
            return true;
        }
        for (AnimalType type: subTypes) {
            if (type.hasInstance(animal)) {
                return  true;
            }
        }
        return false;
    }

    public AnimalType getSuperType() {
        return superType;
    }

    public void setSuperType(AnimalType superType) {
        this.superType = superType;
    }

    public Iterator<AnimalType> getSubTypesIterator() {
        return subTypes.iterator();
    }

    public void addSubType(AnimalType animalType) {
        assert(animalType != null): "AnimalType is null";
        animalType.setSuperType(this);
        subTypes.add(animalType);
    }

    public boolean hasSubType(AnimalType subType) { //is subType a subType of this?
        AnimalType type = subType.superType;
        while (type != null) {
            if(type.equals(this)) {
                return true;
            }
            type = type.getSuperType();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnimalClass() {
        return animalClass;
    }

    public void setAnimalClass(String animalClass) {
        this.animalClass = animalClass;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalType that = (AnimalType) o;
        return vegetarian == that.isVegetarian() && name.equals(that.getName()) && animalClass.equals(that.getAnimalClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, animalClass, vegetarian);
    }
}
