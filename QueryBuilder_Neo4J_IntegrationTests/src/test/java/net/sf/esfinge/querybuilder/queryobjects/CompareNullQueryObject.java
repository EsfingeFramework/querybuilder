package net.sf.esfinge.querybuilder.queryobjects;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Contains;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;

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
