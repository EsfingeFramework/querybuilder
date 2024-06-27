package com.example.polyglot.entities;

import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.annotation.PolyglotJoin;
import esfinge.querybuilder.core.annotation.PolyglotOneToMany;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity
@PersistenceType(value = "MONGODB", secondary = "JPA1")
@Data
public class Pessoa {

    @Id
    private ObjectId id;
    private String name;
    private String lastName;
    private Integer age;
    @Transient
    @PolyglotOneToMany(referencedEntity = Pessoa.class)
    @PolyglotJoin(name = "person", referencedAttributeName = "id")
    private List<Endereco> addresses;
}
