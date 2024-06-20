package com.example.polyglot.entities;

import esfinge.querybuilder.core.annotation.ExternalPersistence;
import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity
@PersistenceType(value = "MONGODB", secondary = "JPA1")
public class Person {

    @Id
    private Integer id;
    private String name;
    private String lastName;
    private Integer age;
    @Transient
    @ExternalPersistence
    @PolyglotOneToOne(mappedBy = "person", joinColumn = "id")
    private Address address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
