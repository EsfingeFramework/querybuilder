package org.esfinge.virtuallab.demo.polyglot;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import esfinge.querybuilder.core.annotation.PersistenceType;
import java.io.Serializable;
import lombok.Data;
import org.bson.types.ObjectId;

@Entity
@PersistenceType("MONGODB")
@Data
public class Address implements Serializable {

    @Id
    private ObjectId id;
    private String city;
    private String uf;
}
