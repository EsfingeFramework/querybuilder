package esfinge.querybuilder.mongodb_tests;

import esfinge.querybuilder.mongodb.MongoDBEntityClassProvider;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestMongoDBEntityClassProvider {

    @Test
    public void getEntityClass() {
        var provider = new MongoDBEntityClassProvider();
        assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
        assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));
    }

    @Test
    public void entityClassNotFound() {
        var provider = new MongoDBEntityClassProvider();
        assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
    }

}
