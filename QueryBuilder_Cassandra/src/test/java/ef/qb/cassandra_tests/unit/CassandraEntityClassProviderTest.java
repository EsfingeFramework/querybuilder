package ef.qb.cassandra_tests.unit;

import ef.qb.cassandra.CassandraEntityClassProvider;
import ef.qb.cassandra_tests.testresources.Address;
import ef.qb.cassandra_tests.testresources.Person;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CassandraEntityClassProviderTest {

    @Test
    public void getEntityClassTest() {
        var provider = new CassandraEntityClassProvider();
        assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
        assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));
    }

    @Test
    public void entityClassNotFoundTest() {
        var provider = new CassandraEntityClassProvider();
        assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
    }

}
