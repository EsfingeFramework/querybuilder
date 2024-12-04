package model;

import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "VISITED")
@Data
@PersistenceType(value = NEO4J)
public class Transition implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private Start customer;
    @EndNode
    private Stage page;
    private Date timestamp;
}
