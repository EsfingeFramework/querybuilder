package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

public class SimpleQueryObject {

    private String lastName;
    private Integer age;
    //private String addressState;

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

	/*
	public String getAddressState() {
		return addressState;
	}
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}*/

}
