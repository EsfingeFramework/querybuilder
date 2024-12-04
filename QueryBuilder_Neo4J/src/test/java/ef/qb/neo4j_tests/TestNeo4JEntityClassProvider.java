package ef.qb.neo4j_tests;

import ef.qb.neo4j.Neo4JEntityClassProvider;
import ef.qb.neo4j_tests.domain.Address;
import ef.qb.neo4j_tests.domain.Person;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestNeo4JEntityClassProvider {

    @Test
    public void getEntityClass() {
        var provider = new Neo4JEntityClassProvider();
        assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
        assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));

    }

    @Test
    public void entityClassNotFound() {
        var provider = new Neo4JEntityClassProvider();
        assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
    }

}
