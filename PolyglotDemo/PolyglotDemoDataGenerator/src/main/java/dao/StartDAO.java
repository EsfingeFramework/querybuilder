package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Start;

@TargetEntity(Start.class)
public interface StartDAO extends Repository<Start> {

    List<Start> getStart();

}
