package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Rating;

@TargetEntity(Rating.class)
public interface RatingDAO extends Repository<Rating> {

    List<Rating> getRating();

}
