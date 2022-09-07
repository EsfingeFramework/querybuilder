package net.sf.esfinge.querybuilder.cassandra.unit.reflection;

import java.util.Objects;

public class TestClass {

    private int id;
    private String name;
    private String lastName;


    public TestClass(int id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
    }

    public void getWhatever() {

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

    @Override
    public String toString() {
        return "TestClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClass testClass = (TestClass) o;
        return id == testClass.id && Objects.equals(name, testClass.name) && Objects.equals(lastName, testClass.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName);
    }
}
