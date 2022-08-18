package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;

public class ComplexQueryObject {

    private String lastName;
    private String addressState;
    private String lastNameStarts;

    @CompareToNull
    private String name;

    @IgnoreWhenNull
    private Integer age;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getLastNameStarts() {
        return lastNameStarts;
    }

    public void setLastNameStarts(String lastNameStarts) {
        this.lastNameStarts = lastNameStarts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
