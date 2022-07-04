package net.sf.esfinge.querybuilder.cassandradb;

import net.sf.esfinge.querybuilder.cassandradb.testresources.Address;
import net.sf.esfinge.querybuilder.cassandradb.testresources.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class CassandraDBEntityClassProviderTest {

    @Test
    public void getEntityClass(){
        CassandraDBEntityClassProvider provider = new CassandraDBEntityClassProvider();
        assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
        assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));

    }

    @Test
    public void entityClassNotFound(){
        CassandraDBEntityClassProvider provider = new CassandraDBEntityClassProvider();
        assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
    }

}