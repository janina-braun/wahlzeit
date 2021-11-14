package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalPhoto extends Photo{
    private Animal animal = null;

    public AnimalPhoto() {
        super();
    }

    public AnimalPhoto(PhotoId myId) {
        super(myId);
    }

    public AnimalPhoto(Animal animal) {
        super();
        this.animal = animal;
    }

    public AnimalPhoto(PhotoId myId, Animal animal) {
        super(myId);
        this.animal = animal;
    }

    public AnimalPhoto(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        if (animal != null) {
            animal.setName(rset.getString("animal_name"));
            animal.setAnimalClass(rset.getString("animal_class"));
            animal.setAvg_weight(rset.getDouble("avg_weight"));
            animal.setVegetarian(rset.getBoolean("vegetarian"));
            animal.setHabitat(rset.getString("habitat"));
        } else {
            this.animal = new Animal(rset.getString("animal_name"),
                                     rset.getString("animal_class"),
                                     rset.getDouble("avg_weight"),
                                     rset.getBoolean("vegetarian"),
                                     rset.getString("habitat"));

        }
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        if (animal != null) {
            rset.updateString("animal_name", animal.getName());
            rset.updateString("animal_class", animal.getAnimalClass());
            rset.updateDouble("avg_weight", animal.getAvg_weight());
            rset.updateBoolean("vegetarian", animal.isVegetarian());
            rset.updateString("habitat", animal.getHabitat());
        }
    }
}
