package org.esfinge.querybuilder.jdbc.testresources;

import org.esfinge.querybuilder.annotations.Column;
import org.esfinge.querybuilder.annotations.ID;
import org.esfinge.querybuilder.annotations.Table;

@Table(name="worker")
public class Worker {

	@ID
	@Column(name = "id")
	private Integer id;
	private String name;
	private String lastName;
	private Integer age;
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
