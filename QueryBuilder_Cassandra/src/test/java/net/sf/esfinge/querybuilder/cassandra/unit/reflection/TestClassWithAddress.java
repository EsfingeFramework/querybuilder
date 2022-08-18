package net.sf.esfinge.querybuilder.cassandra.unit.reflection;

import java.util.Objects;

public class TestClassWithAddress {

    private int id;
    private String name;
    private String lastName;
    private TestAddress address;

    public TestClassWithAddress(int id, String name, String lastName, TestAddress address) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public TestAddress getAddress() {
        return address;
    }

    public void setAddress(TestAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "TestClassWithAddress{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClassWithAddress that = (TestClassWithAddress) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lastName, that.lastName) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, address);
    }
}
