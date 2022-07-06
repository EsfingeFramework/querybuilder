package net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration;

import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;

public class ClassWithMissingAnnotation implements CassandraEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
