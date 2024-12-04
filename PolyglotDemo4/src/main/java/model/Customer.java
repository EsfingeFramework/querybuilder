package model;

import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.PolyglotJoin;
import ef.qb.core.annotation.PolyglotOneToMany;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Data
@PersistenceType(value = JPA1, secondary = NEO4J)
public class Customer implements Serializable {

    @Id
    @SequenceGenerator(name = "customerSequence", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "customerSequence")
    private Long id;
    private String login;
    private String name;
    private String email;
    private String phone;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
    @Transient
    @PolyglotOneToMany(referencedEntity = Customer.class)
    @PolyglotJoin(name = "customer.customer", referencedAttributeName = "login")
    private List<Transition> transitions;
}
