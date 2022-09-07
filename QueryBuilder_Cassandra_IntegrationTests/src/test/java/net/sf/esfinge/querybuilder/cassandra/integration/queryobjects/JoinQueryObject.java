package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

public class JoinQueryObject {

    private String lastName;
    private String addressState;

    private String addressCity;

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

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }
}
