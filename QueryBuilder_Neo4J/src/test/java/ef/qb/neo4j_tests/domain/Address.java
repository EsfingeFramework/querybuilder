package ef.qb.neo4j_tests.domain;

import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@PersistenceType(NEO4J)
public class Address {

    @Id
    private Long graphId;

    @Index
    private String city;
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
