package model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ef.qb.cassandra.entity.CassandraEntity;
import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import java.util.UUID;
import lombok.Data;

@Table(keyspace = "store",
        name = "item",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM")
@Data
@PersistenceType(CASSANDRA)
public class Item implements CassandraEntity {

    @PartitionKey
    private UUID cart;
    private String product;
    private Integer quantity;
    private Double price;

}
