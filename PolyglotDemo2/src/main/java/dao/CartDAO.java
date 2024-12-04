package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.Condition;
import ef.qb.core.annotation.DomainTerm;
import ef.qb.core.annotation.TargetEntity;
import ef.qb.core.methodparser.ComparisonType;
import java.util.List;
import model.Cart;

@TargetEntity(Cart.class)
@DomainTerm(term = "active", conditions = @Condition(property = "status", comparison = ComparisonType.EQUALS, value = "active"))
public interface CartDAO extends Repository<Cart> {

    List<Cart> getCart();

    List<Cart> getCartActive();

}
