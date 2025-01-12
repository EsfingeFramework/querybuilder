package model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ef.qb.cassandra.entity.CassandraEntity;
import ef.qb.core.annotation.PersistenceType;
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
    private Date timestamp;
    private String customer;
    private String status;

    public Cart() {
        this.id = UUID.randomUUID();
    }

}
