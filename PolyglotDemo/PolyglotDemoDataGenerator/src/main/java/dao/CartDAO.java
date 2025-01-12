package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Cart;

@TargetEntity(Cart.class)
public interface CartDAO extends Repository<Cart> {

    List<Cart> getCart();

}
