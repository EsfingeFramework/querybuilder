package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

public class SpecialComparisonQueryObject {

    private String lastNameStarts;
    private Integer age;

    public String getLastNameStarts() {
        return lastNameStarts;
    }

    public void setLastNameStarts(String lastNameStarts) {
        this.lastNameStarts = lastNameStarts;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
