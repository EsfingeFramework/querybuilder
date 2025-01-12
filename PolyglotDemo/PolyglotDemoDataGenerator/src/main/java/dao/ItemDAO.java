package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Item;

@TargetEntity(Item.class)
public interface ItemDAO extends Repository<Item> {

    List<Item> getItem();

}
