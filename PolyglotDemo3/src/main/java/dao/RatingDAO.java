package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.Condition;
import ef.qb.core.annotation.DomainTerm;
import ef.qb.core.annotation.TargetEntity;
import ef.qb.core.methodparser.ComparisonType;
import java.util.List;
import model.Rating;

@TargetEntity(Rating.class)
@DomainTerm(term = "max", conditions = @Condition(property = "value", comparison = ComparisonType.EQUALS, value = "5"))
public interface RatingDAO extends Repository<Rating> {

    List<Rating> getRatingMax();

}
