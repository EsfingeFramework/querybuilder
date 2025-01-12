package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Product;

@TargetEntity(Product.class)
public interface ProductDAO extends Repository<Product> {

    List<Product> getProduct();

    List<Product> getProductByName(String name);

}
