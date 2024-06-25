package com.example.polyglot.entities;

import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity
@PersistenceType(value = "MONGODB", secondary = "JPA1")
@Data
public class Person {

    @Id
    private Integer id;
    private String name;
    private String lastName;
    private Integer age;
    private Integer addressId;
    @Transient
    @PolyglotOneToOne(joinAttribute = "addressId", referencedAttributeKey = "id")
    private Address address;
}
