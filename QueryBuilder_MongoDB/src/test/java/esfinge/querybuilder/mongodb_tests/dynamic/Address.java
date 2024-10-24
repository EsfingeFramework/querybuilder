package esfinge.querybuilder.mongodb_tests.dynamic;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import esfinge.querybuilder.core.annotation.PersistenceType;

@Entity
@PersistenceType("MONGODB")
public class Address {

    @Id
    private int id;
    private String city;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
