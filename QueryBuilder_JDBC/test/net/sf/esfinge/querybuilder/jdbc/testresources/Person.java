package net.sf.esfinge.querybuilder.jdbc.testresources;

import net.sf.esfinge.querybuilder.annotations.Column;
import net.sf.esfinge.querybuilder.annotations.ID;
import net.sf.esfinge.querybuilder.annotations.JoinColumn;
import net.sf.esfinge.querybuilder.annotations.Table;

@Table(name="person")
public class Person {

	@ID
	@Column(name = "id")
	private Integer id;

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

	private String name;
	private String lastName;
	private Integer age;

	@Table(name = "address")
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
	private Address address;

}
