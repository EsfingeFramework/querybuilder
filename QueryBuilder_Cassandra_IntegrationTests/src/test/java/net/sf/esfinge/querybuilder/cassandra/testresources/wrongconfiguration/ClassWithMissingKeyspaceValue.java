package net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;

@Table(name = "classwithmissingkeyspacevalue",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class ClassWithMissingKeyspaceValue implements CassandraEntity {

    @PartitionKey
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
