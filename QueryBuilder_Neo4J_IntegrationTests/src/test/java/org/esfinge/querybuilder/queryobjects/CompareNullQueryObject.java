package org.esfinge.querybuilder.queryobjects;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.Contains;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;

public class CompareNullQueryObject {
	
	@Contains @IgnoreWhenNull
	private String name;
	
	@Contains @CompareToNull 
	private String lastName;

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

}
