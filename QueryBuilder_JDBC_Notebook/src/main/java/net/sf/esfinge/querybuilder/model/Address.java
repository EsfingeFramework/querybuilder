package net.sf.esfinge.querybuilder.model;

import net.sf.esfinge.querybuilder.annotations.Column;
import net.sf.esfinge.querybuilder.annotations.ID;

public class Address {

	@ID
	@Column(name = "addressid")
	private int addressId;

	private String addressStreet;
	private String addressStreetNumber;
	private String addressStreetComplement;
	private String addressCity;
	private String addressState;
	private String addressZipcode;

	public String getAddressStreetNumber() {
		return addressStreetNumber;
	}

	public void setAddressStreetNumber(String addressStreetNumber) {
		this.addressStreetNumber = addressStreetNumber;
	}

	public String getAddressStreetComplement() {
		return addressStreetComplement;
	}

	public void setAddressStreetComplement(String addressStreetComplement) {
		this.addressStreetComplement = addressStreetComplement;
	}

	public String getAddressZipcode() {
		return addressZipcode;
	}

	public void setAddressZipcode(String addressZipcode) {
		this.addressZipcode = addressZipcode;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

}
