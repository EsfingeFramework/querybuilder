package model;

import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import java.io.Serializable;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@PersistenceType(NEO4J)
public class Start implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Index
    private String customer;
    private String session;
}
