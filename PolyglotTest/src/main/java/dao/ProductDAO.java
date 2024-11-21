package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Product;

@TargetEntity(Product.class)
public interface ProductDAO extends Repository<Product> {

    List<Product> getProduct();

    List<Product> getProductByName(String name);

    List<Product> getProductByDescription(String description);

    List<Product> getProductByPrice(double price);

    List<Product> getProductByWeight(int weight);

    List<Product> getProductByBrand(String brand);

    List<Product> getProductBySegment(String segment);

    List<Product> getProductByType(String type);

}
