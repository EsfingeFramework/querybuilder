package net.sf.esfinge.querybuilder.model.persistence;

import java.util.List;

import net.sf.esfinge.querybuilder.model.Address;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;

public interface AddressOperations extends Repository<Address> {

	public List<Address> getAddress();

	public List<Address> getAddressOrderByAddressIdDesc();

	public List<Address> getAddressByAddressCityStartsAndAddressStateStarts(
			@IgnoreWhenNull String city, @IgnoreWhenNull String state);

}
