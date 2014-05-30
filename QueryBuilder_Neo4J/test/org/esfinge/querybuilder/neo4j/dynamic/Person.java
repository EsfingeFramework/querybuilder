package org.esfinge.querybuilder.neo4j.dynamic;

import org.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.Indexed;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.RelatedTo;

@NodeEntity
public class Person {
	
	@Id
	private Integer id;
	@Indexed
	private String name;
	@Indexed
	private String lastName;
	@Indexed
	private Integer age;
	
	@RelatedTo(targetClass = Address.class)
	private Address address;

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	

}
