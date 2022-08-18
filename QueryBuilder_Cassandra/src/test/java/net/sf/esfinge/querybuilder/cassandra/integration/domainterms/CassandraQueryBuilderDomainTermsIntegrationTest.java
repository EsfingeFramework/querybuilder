package net.sf.esfinge.querybuilder.cassandra.integration.domainterms;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabasePersonIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraQueryBuilderDomainTermsIntegrationTest extends CassandraBasicDatabasePersonIntegrationTest {

    CassandraTestDomainQuery testQuery = QueryBuilder.create(CassandraTestDomainQuery.class);

    @Test
    public void domainQueryTest() {
        List<Person> list = testQuery.getPersonTeenager();
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The list should have Pedro", "Pedro", list.get(0).getName());
        assertEquals("The list should have Maria", "Maria", list.get(1).getName());
    }

    @Test
    public void domainTermWithTwoWordsTest() {
        List<Person> list = testQuery.getPersonAdultGuys();
        assertEquals("The list should have 2 person", 2, list.size());
        assertEquals("The list should have Antonio", "Antonio", list.get(0).getName());
        assertEquals("The list should have Marcos", "Marcos", list.get(1).getName());
    }

    @Test
    public void twoDomainTermsTest() {
        List<Person> list = testQuery.getPersonSilvaFamilyAdultGuys();
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
    }

    @Test
    public void domainTermWithParameterTest() {
        List<Person> list = testQuery.getPersonSilvaFamilyByAge(25);
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
    }

    @Test
    public void twoDomainTermsWithJoinTest() {
        List<Person> list = testQuery.getPersonSilvaFamilyFromCampos();
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
    }

    @Test
    public void domainTermWithSpecialComparisonTest() {
        List<Person> list = testQuery.getPersonNameStartsWithM();
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The list should have Maria", "Maria", list.get(0).getName());
        assertEquals("The list should have Marcos", "Marcos", list.get(1).getName());
    }
}
