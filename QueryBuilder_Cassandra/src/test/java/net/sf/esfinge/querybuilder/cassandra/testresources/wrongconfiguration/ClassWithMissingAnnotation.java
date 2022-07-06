package net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration;

import com.datastax.driver.mapping.annotations.PartitionKey;
import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;

public class ClassWithMissingAnnotation implements CassandraEntity {

    @PartitionKey
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
