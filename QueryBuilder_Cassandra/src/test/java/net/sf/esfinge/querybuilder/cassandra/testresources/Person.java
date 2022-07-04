package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.cassandra.annotations.ID;
import net.sf.esfinge.querybuilder.cassandra.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;

@Table(name = "person")
public class Person implements CassandraEntity {

    @ID
    private Integer id;
    private String name;
    private String lastName;
    private Integer age;
    private Address address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
