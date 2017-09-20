package net.sf.esfinge.querybuilder.neo4j.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Person {
	
	@GraphId
	private Long graphId;

	@Index
	private Integer id;
	private String name;
	private String lastName = null;
	private Integer age;
	
	@Relationship(type = "LIVES", direction = Relationship.OUTGOING)
	private Address address;

	public Long getGraphId() {
		return graphId;
	}
	
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
		if(address != null) {
			return id + " " + name + " " + lastName + " " + age + " " + address.getId() + " " + address.getCity() + " " + address.getState();
		} else {
			return id + " " + name + " " + lastName + " " + age + " null";
		}
	}
}
