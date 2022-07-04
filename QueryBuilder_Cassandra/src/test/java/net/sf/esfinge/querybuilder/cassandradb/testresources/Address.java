package net.sf.esfinge.querybuilder.cassandradb.testresources;

import net.sf.esfinge.querybuilder.cassandradb.annotations.ID;
import net.sf.esfinge.querybuilder.cassandradb.annotations.Table;
import net.sf.esfinge.querybuilder.cassandradb.entity.CassandraDBEntity;

@Table(name = "address")
public class Address implements CassandraDBEntity {

    @ID
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
