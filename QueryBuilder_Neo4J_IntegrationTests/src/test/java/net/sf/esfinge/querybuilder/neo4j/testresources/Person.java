package net.sf.esfinge.querybuilder.neo4j.testresources;

import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.Indexed;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.RelatedTo;


@NodeEntity
public class Person {
	
	@Id
	private Integer id;
	@Indexed
	private String name;
	@Indexed
	private String lastName = null;
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
	
	@Override
	public String toString(){
		return id + " " + name + " " + lastName + " " + age + " " + address.getId() + " " + address.getCity() + " " + address.getState();
	}
}
