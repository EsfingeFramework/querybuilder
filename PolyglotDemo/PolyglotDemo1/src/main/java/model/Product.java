package model;

import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.PolyglotJoin;
import ef.qb.core.annotation.PolyglotOneToMany;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import static ef.qb.core.utils.PersistenceTypeConstants.MONGODB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Data
@PersistenceType(value = JPA1, secondary = MONGODB)
public class Product implements Serializable {

    @Id
    @SequenceGenerator(name = "productSequence", sequenceName = "product_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "productSequence")
    private Long id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String page;
    @Transient
    @PolyglotOneToMany(referencedEntity = Product.class)
    @PolyglotJoin(name = "product", referencedAttributeName = "sku")
    private List<Rating> ratings;
}
