package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Customer;

@TargetEntity(Customer.class)
public interface CustomerDAO extends Repository<Customer> {

    List<Customer> getCustomer();

}
