package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabasePersonIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraQueryObjectIntegrationTest extends CassandraBasicDatabasePersonIntegrationTest {

    private final TestQueryObject testQuery = QueryBuilder.create(TestQueryObject.class);

    @Test
    public void simpleQueryObjectTest() {
        SimpleQueryObject qo = new SimpleQueryObject();
        qo.setLastName("Silva");
        qo.setAge(20);
        Person p = testQuery.getPerson(qo);

        assertEquals("Pedro", p.getName());
    }


    @Test
    public void queryObjectWithComparisonTypeTest() {
        ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
        qo.setAgeGreater(25);
        qo.setAgeLesser(40);
        List<Person> list = testQuery.getPerson(qo);
        Person p = list.get(0);

        assertEquals(1, list.size());
        assertEquals("Antonio", p.getName());
    }

    @Test
    public void queryObjectWithNullComparisonTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, 'NullPerson', null, 20)";

        session.execute(query);
        session.close();

        CompareNullQueryObject qo = new CompareNullQueryObject();
        qo.setLastName(null);
        List<Person> list = testQuery.getPerson(qo);

        assertEquals("NullPerson", list.get(0).getName());
    }

    @Test
    public void queryObjectIgnoreNullTest() {
        IgnoreNullQueryObject qo = new IgnoreNullQueryObject();
        qo.setLastName("Silva");
        List<Person> list = testQuery.getPerson(qo);
        assertEquals(2, list.size());
        Person p = list.get(0);
        assertEquals(new Integer(1), p.getId());
    }

    @Test
    public void queryObjectIgnoreNullWithNullValueTest() {
        IgnoreNullQueryObject qo = new IgnoreNullQueryObject();
        qo.setLastName(null);
        List<Person> list = testQuery.getPerson(qo);
        assertEquals(5, list.size());
    }

    @Test
    public void queryObjectWithOrderByAscendantTest() {
        ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
        qo.setAgeGreater(1);
        qo.setAgeLesser(100);

        List<Person> list = testQuery.getPersonOrderByNameAsc(qo);
        assertEquals("Antonio", list.get(0).getName());
        assertEquals("Silvia", list.get(list.size() - 1).getName());
    }

    @Test
    public void queryObjectWithOrderByDescendantTest() {
        ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
        qo.setAgeGreater(1);
        qo.setAgeLesser(100);

        List<Person> list = testQuery.getPersonOrderByNameDesc(qo);
        assertEquals("Silvia", list.get(0).getName());
        assertEquals("Antonio", list.get(list.size() - 1).getName());
    }

    @Test
    public void queryObjectWithSpecialComparisonTest() {
        SpecialComparisonQueryObject qo = new SpecialComparisonQueryObject();
        qo.setAge(20);
        qo.setLastNameStarts("Si");

        List<Person> list = testQuery.getPersonOrderByAge(qo);

        assertEquals("Pedro", list.get(0).getName());
    }

    @Test
    public void queryObjectWithJoinTest() {
        JoinQueryObject qo = new JoinQueryObject();
        qo.setLastName("Silva");
        qo.setAddressState("MG");
        qo.setAddressCity("Juiz de Fora");

        List<Person> list = testQuery.getPersonOrderByLastName(qo);

        assertEquals("Pedro", list.get(0).getName());
    }

    @Test
    public void complexQueryObjectTest() {
        ComplexQueryObject qo = new ComplexQueryObject();
        qo.setLastName("Silva");
        qo.setAddressState("MG");
        qo.setLastNameStarts("Si");
        qo.setName("Pedro");
        qo.setAge(null);

        List<Person> list = testQuery.getPersonOrderById(qo);

        assertEquals("Pedro", list.get(0).getName());
    }


}
