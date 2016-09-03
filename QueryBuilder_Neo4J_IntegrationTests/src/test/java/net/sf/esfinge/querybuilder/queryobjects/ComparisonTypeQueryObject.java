package net.sf.esfinge.querybuilder.queryobjects;

import net.sf.esfinge.querybuilder.annotation.Contains;

public class ComparisonTypeQueryObject {
	
	private Integer ageGreater;
	private Integer ageLesser;
	
	@Contains
	private String name;
	private String lastName;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Contains
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
