package net.sf.esfinge.querybuilder.cassandra.integration.nullvalues;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraQueryBuilderNullValuesTest extends CassandraBasicDatabaseTest {

    private final CassandraTestNullValueQueries testQuery = QueryBuilder.create(CassandraTestNullValueQueries.class);

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void compareToNullQueryTest() {
        testQuery.getPersonByName(null);
    }

    @Test
    public void ignoreWhenNullQueryTest() {
        List<Person> list = testQuery.getPersonByAgeGreater(null);
        assertEquals("The list should have 5 persons", 5, list.size());

        list = testQuery.getPersonByAgeGreater(18);
        assertEquals("The list should have 4 persons", 4, list.size());
    }

    @Test
    public void ignoreWhenNullWithTwoParamsOneNullTest() {
        List<Person> list = testQuery.getPersonByNameAndLastName("Marcos", null);
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The person should be Marcos", "Marcos", list.get(0).getName());

        list = testQuery.getPersonByNameAndLastName(null, "Silva");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first person should be Pedro", "Pedro", list.get(0).getName());
        assertEquals("The first person should be Marcos", "Marcos", list.get(1).getName());
    }

    @Test
    public void ignoreWhenNullWithTwoParamsNotNullTest() {
        List<Person> list = testQuery.getPersonByNameAndLastName("Marcos", "Silva");
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The first person should be Marcos", "Marcos", list.get(0).getName());
    }

    @Test
    public void ignoreWhenNullWithTwoParamsBothNullTest() {
        List<Person> list = testQuery.getPersonByNameAndLastName(null, null);
        assertEquals("The list should have 5 persons", 5, list.size());
    }


}
