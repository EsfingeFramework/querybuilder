package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Transition;

@TargetEntity(Transition.class)
public interface TransitionDAO extends Repository<Transition> {

    List<Transition> getTransition();

}
