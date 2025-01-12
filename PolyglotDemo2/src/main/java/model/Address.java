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
public class Address implements Serializable {

    @Id
    @SequenceGenerator(name = "addressSequence", sequenceName = "address_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "addressSequence")
    private Long id;
    private String city;
    private String street;
    private Long number;
    private String complement;
    private String federalunit;
    private String zipcode;

}
