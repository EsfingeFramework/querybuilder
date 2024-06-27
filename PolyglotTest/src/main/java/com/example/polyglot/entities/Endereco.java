package com.example.polyglot.entities;

import esfinge.querybuilder.core.annotation.PersistenceType;
import java.io.Serializable;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;

@Entity
@PersistenceType("JPA1")
@Data
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String uf;
    @Convert(converter = ObjectIdConverter.class)
    private ObjectId person;
}
