package model;

import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
@PersistenceType(JPA1)
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
}
