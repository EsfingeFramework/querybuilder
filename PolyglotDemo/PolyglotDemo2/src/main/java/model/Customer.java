package model;

import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
@PersistenceType(value = JPA1)
public class Customer implements Serializable {

    @Id
    @SequenceGenerator(name = "customerSequence", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "customerSequence")
    private Long id;
    private String login;
    private String name;
    private String email;
    private String phone;
    @OneToOne(fetch = LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id")
    private Address address;
}
