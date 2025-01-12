package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Stage;

@TargetEntity(Stage.class)
public interface StageDAO extends Repository<Stage> {

    List<Stage> getStage();

}
