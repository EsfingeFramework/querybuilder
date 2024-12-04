package model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ef.qb.cassandra.entity.CassandraEntity;
import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import java.util.Date;
import java.util.UUID;
import lombok.Data;

@Table(keyspace = "store",
        name = "cart",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM")
@Data
@PersistenceType(CASSANDRA)
public class Cart implements CassandraEntity {

    @PartitionKey
    private UUID id;
    @ClusteringColumn
    private Date timestamp;
    private String customer;
    private String status;

}
