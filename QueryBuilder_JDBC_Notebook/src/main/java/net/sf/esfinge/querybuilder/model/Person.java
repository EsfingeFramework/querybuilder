package net.sf.esfinge.querybuilder.model;

import net.sf.esfinge.querybuilder.annotations.Column;
import net.sf.esfinge.querybuilder.annotations.ID;

public class Person {

	@ID
	@Column(name = "personid")
	private int personId;

	private String personName;
	private String personSurName;
	private String personBirthDate;

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonSurName() {
		return personSurName;
	}

	public void setPersonSurName(String personSurName) {
		this.personSurName = personSurName;
	}

	public String getPersonBirthDate() {
		return personBirthDate;
	}

	public void setPersonBirthDate(String personBirthDate) {
		this.personBirthDate = personBirthDate;
	}

}
