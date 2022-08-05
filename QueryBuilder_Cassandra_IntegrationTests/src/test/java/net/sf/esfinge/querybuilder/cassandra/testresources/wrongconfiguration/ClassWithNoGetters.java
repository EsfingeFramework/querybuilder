package net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "test", name = "classwithnogetters",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class ClassWithNoGetters {

    @PartitionKey
    String name;

    public void setName(String name) {
        this.name = name;
    }

}
