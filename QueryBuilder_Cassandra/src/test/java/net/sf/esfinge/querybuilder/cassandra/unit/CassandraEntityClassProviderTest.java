package net.sf.esfinge.querybuilder.cassandra.unit;

import net.sf.esfinge.querybuilder.cassandra.CassandraEntityClassProvider;
import net.sf.esfinge.querybuilder.cassandra.testresources.Address;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CassandraEntityClassProviderTest {

    @Test
    public void getEntityClassTest() {
        CassandraEntityClassProvider provider = new CassandraEntityClassProvider();
        assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
        assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));
    }

    @Test
    public void entityClassNotFoundTest() {
        CassandraEntityClassProvider provider = new CassandraEntityClassProvider();
        assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
    }

}