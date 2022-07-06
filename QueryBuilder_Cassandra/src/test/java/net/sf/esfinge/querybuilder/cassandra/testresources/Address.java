package net.sf.esfinge.querybuilder.cassandra.testresources;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;

@Table(keyspace = "test", name = "address",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Address implements CassandraEntity {

    @PartitionKey
    private int id;
    private String city;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
