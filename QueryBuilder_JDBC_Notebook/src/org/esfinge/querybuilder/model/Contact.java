package org.esfinge.querybuilder.model;

import org.esfinge.querybuilder.annotations.Column;
import org.esfinge.querybuilder.annotations.ID;
import org.esfinge.querybuilder.annotations.JoinColumn;
import org.esfinge.querybuilder.annotations.Table;

public class Contact {

	@ID
	@Column(name = "contactid")
	private int contactId;

	@Table(name = "person")
	@JoinColumn(name = "contactpersonid", referencedColumnName = "personid")
	private Person contactPersonId;

	@Table(name = "address")
	@JoinColumn(name = "contactaddressid", referencedColumnName = "addressid")
	private Address contactAddressId;

	private String contactEmail;
	private String contactPhoneNumber;
	private String contactCellNumber;

	private String contactType;
	
	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public Person getContactPersonId() {
		return contactPersonId;
	}

	public void setContactPersonId(Person contactPersonId) {
		this.contactPersonId = contactPersonId;
	}

	public Address getContactAddressId() {
		return contactAddressId;
	}

	public void setContactAddressId(Address contactAddressId) {
		this.contactAddressId = contactAddressId;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	public String getContactCellNumber() {

		return contactCellNumber;

	}

	public void setContactCellNumber(String contactCellNumber) {
		this.contactCellNumber = contactCellNumber;
	}

}
