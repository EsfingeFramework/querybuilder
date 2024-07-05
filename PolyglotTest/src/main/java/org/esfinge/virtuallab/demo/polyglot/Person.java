package org.esfinge.virtuallab.demo.polyglot;

import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.annotation.PolyglotJoin;
import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import lombok.Data;
import org.bson.types.ObjectId;
import org.esfinge.virtuallab.polyglot.ObjectIdConverter;

@Entity
@PersistenceType(value = "JPA1", secondary = "MONGODB")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "personSequence")
    @SequenceGenerator(name = "personSequence", sequenceName = "person_id_seq", allocationSize = 1)
    private Integer id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private Integer age;
    @Convert(converter = ObjectIdConverter.class)
    @Column(name = "address_id")
    private ObjectId addressId;
    @Transient
    @PolyglotOneToOne(referencedEntity = Address.class)
    @PolyglotJoin(name = "addressId", referencedAttributeName = "id")
    private Address address;
}
