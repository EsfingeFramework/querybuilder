package net.sf.esfinge.querybuilder.cassandra.integration.main;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.NotEnoughExamplesException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabasePersonIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSimpleTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithNoGetters;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithNoGettersTestQuery;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraRepositoryIntegrationTest extends CassandraBasicDatabasePersonIntegrationTest {

    CassandraSimpleTestQuery testQuery = QueryBuilder.create(CassandraSimpleTestQuery.class);

    @Test
    public void saveTest() {
        Person expected = new Person();
        expected.setId(6);
        expected.setName("testname");
        expected.setLastName("testlastname");
        expected.setAge(30);

        testQuery.save(expected);

        MappingManager manager = new MappingManager(CassandraTestUtils.getSession());
        Mapper<Person> mapper = manager.mapper(Person.class);

        Person actual = mapper.get(6);

        assertEquals("New Person should be added to the persons", expected, actual);
    }

    @Test
    public void deleteTest() {
        testQuery.delete(2);

        MappingManager manager = new MappingManager(CassandraTestUtils.getSession());
        Mapper<Person> mapper = manager.mapper(Person.class);

        Person actual = mapper.get(2);

        assertNull("Should not retrieve any person", actual);
    }

    @Test
    public void listTest() {
        List<Person> list = testQuery.list();
        assertEquals("The list should have 5 persons", 5, list.size());
    }

    @Test
    public void getByIdTest() {
        Person expected = new Person();
        expected.setId(3);
        expected.setName("Marcos");
        expected.setLastName("Silva");
        expected.setAge(50);

        Person actual = testQuery.getById(3);

        assertEquals("Should retrieve person Marcos Silva", expected, actual);
    }

    @Test
    public void queryByExampleTest() {
        Person example = new Person();
        example.setName("Pedro");

        List<Person> list = testQuery.queryByExample(example);

        assertEquals("The list should have 1 person", 1, list.size());
    }

    @Test
    public void queryByExampleWithNoGettersTest() {
        ClassWithNoGettersTestQuery noGettersTestQuery = QueryBuilder.create(ClassWithNoGettersTestQuery.class);

        ClassWithNoGetters example = new ClassWithNoGetters();
        example.setName("Pedro");

        assertThrows(NotEnoughExamplesException.class, () -> noGettersTestQuery.queryByExample(example));
    }

    @Test
    public void queryByTwoExamplesTest() {
        Person example = new Person();
        example.setName("Pedro");
        example.setAge(20);

        List<Person> list = testQuery.queryByExample(example);

        assertEquals("The list should have 1 person", 1, list.size());
    }

}
