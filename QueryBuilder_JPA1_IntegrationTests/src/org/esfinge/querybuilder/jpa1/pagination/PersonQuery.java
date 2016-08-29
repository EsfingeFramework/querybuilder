package org.esfinge.querybuilder.jpa1.pagination;

import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;

public class PersonQuery {

	@Greater
	private Integer age;
	@IgnoreWhenNull
	private String name;
	
	public PersonQuery() {
		
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
