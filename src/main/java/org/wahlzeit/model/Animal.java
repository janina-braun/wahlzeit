package org.wahlzeit.model;

import java.util.Objects;

public class Animal {
    private String name;
    private String animalClass;
    private double avg_weight;
    private boolean vegetarian;
    private String habitat;

    public Animal(String name, String animalClass, double avg_weight, boolean vegetarian, String habitat) {
        this.name = name;
        this.animalClass = animalClass;
        this.avg_weight = avg_weight;
        this.vegetarian = vegetarian;
        this.habitat = habitat;
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

    public double getAvg_weight() {
        return avg_weight;
    }

    public void setAvg_weight(double avg_weight) {
        this.avg_weight = avg_weight;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public boolean isEqual(Animal animal) {
        return this.name.equals(animal.getName()) &&
                this.animalClass.equals(animal.getAnimalClass()) &&
                Math.abs(this.avg_weight - animal.getAvg_weight()) <= 0.1 &&
                this.vegetarian == animal.isVegetarian() &&
                this.habitat.equals(animal.getHabitat());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.isEqual((Animal) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, animalClass, avg_weight, vegetarian, habitat);
    }
}
