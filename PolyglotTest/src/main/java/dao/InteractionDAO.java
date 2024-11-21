package dao;

import ef.qb.core.Repository;
import ef.qb.core.annotation.TargetEntity;
import java.util.List;
import model.Interaction;

@TargetEntity(Interaction.class)
public interface InteractionDAO extends Repository<Interaction> {

    List<Interaction> getInteraction();

    List<Interaction> getInteractionByUser(String user);

    List<Interaction> getInteractionByTimestamp(String timestamp);

    List<Interaction> getInteractionByProduct(String product);

    List<Interaction> getInteractionByType(String type);

    List<Interaction> getInteractionBySource(String source);

    List<Interaction> getInteractionByMetadata(String metadata);
}
