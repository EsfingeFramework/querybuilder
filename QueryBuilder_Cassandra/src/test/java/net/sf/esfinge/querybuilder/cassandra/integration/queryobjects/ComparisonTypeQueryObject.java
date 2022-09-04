package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

public class ComparisonTypeQueryObject {

    private Integer ageGreater;
    private Integer ageLesser;

    public Integer getAgeGreater() {
        return ageGreater;
    }

    public void setAgeGreater(Integer ageGreater) {
        this.ageGreater = ageGreater;
    }

    public Integer getAgeLesser() {
        return ageLesser;
    }

    public void setAgeLesser(Integer ageLesser) {
        this.ageLesser = ageLesser;
    }
}
