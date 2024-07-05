package org.esfinge.virtuallab.demo.polyglot;

import esfinge.querybuilder.core.annotation.PersistenceType;
import java.io.Serializable;
import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
@PersistenceType("MONGODB")
@Data
public class Address implements Serializable {

    @Id
    private ObjectId id;
    private String city;
    private String uf;
}
