package net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration;

import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;

@Table(keyspace = "test", name = "person",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class ClassWithMissingPartitionKey implements CassandraEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
