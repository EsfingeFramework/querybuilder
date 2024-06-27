package com.example.polyglot.entities;

import esfinge.querybuilder.core.annotation.PersistenceType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@PersistenceType("JPA1")
@Data
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSequence")
    @SequenceGenerator(name = "addressSequence", sequenceName = "address_id_seq", allocationSize = 1)
    private Integer id;
    private String city;
    private String uf;
}
