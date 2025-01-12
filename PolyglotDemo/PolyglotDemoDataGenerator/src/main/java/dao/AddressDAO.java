package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Address;

@TargetEntity(Address.class)
public interface AddressDAO extends Repository<Address> {

    List<Address> getAddress();

}
