package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;

public class CompareNullQueryObject {

    @CompareToNull
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "CompareNullQueryObject{" +
                "lastName='" + lastName + '\'' +
                '}';
    }
}
