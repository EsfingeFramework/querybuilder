package model;

import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.PolyglotJoin;
import ef.qb.core.annotation.PolyglotOneToMany;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@PersistenceType(value = JPA1, secondary = CASSANDRA)
@Data
public class Product implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private double price;
    private int weight;
    private String brand;
    private String segment;
    private String type;
    @Transient
    private List<Rating> ratings;
    @Transient
    @PolyglotOneToMany(referencedEntity = Product.class)
    @PolyglotJoin(name = "product", referencedAttributeName = "name")
    private List<Interaction> interactions;
}
