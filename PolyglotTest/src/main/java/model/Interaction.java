package model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ef.qb.cassandra.entity.CassandraEntity;
import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import java.util.Date;
import lombok.Data;

@Table(keyspace = "product_data", name = "interaction",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
@Data
@PersistenceType(CASSANDRA)
public class Interaction implements CassandraEntity {

    @PartitionKey
    private String user;
    @ClusteringColumn
    private Date timestamp;
    private String product;
    private String type;
    private String source;
    private String metadata;
}
