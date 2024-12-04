package model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import ef.qb.cassandra.entity.CassandraEntity;
import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.PolyglotJoin;
import ef.qb.core.annotation.PolyglotOneToOne;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import java.util.Date;
import java.util.UUID;
import lombok.Data;

@Table(keyspace = "store",
        name = "cart",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM")
@Data
@PersistenceType(value = CASSANDRA, secondary = JPA1)
public class Cart implements CassandraEntity {

    @PartitionKey
    private UUID id;
    @ClusteringColumn
    private Date timestamp;
    private String customer;
    private String status;
    @Transient
    @PolyglotOneToOne(referencedEntity = Cart.class)
    @PolyglotJoin(name = "login", referencedAttributeName = "customer")
    private Customer shopper;

}
