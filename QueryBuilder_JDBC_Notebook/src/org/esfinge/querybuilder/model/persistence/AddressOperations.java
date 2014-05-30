package org.esfinge.querybuilder.model.persistence;

import java.util.List;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.model.Address;

public interface AddressOperations extends Repository<Address> {

	public List<Address> getAddress();

	public List<Address> getAddressOrderByAddressIdDesc();

	public List<Address> getAddressByAddressCityStartsAndAddressStateStarts(
			@IgnoreWhenNull String city, @IgnoreWhenNull String state);

}
