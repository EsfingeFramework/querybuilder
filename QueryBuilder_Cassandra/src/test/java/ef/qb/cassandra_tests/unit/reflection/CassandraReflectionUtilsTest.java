package ef.qb.cassandra_tests.unit.reflection;

import ef.qb.cassandra.exceptions.GetterNotFoundInClassException;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGettersForFields;
import java.lang.reflect.Method;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CassandraReflectionUtilsTest {

    @Test
    public void getClassGettersTest() {
        Method[] getters = getClassGetters(TestClass.class);
        List<String> actual = stream(getters).map(Method::getName).collect(toList());

        List<String> expected = new ArrayList<>();
        expected.add("getName");
        expected.add("getLastName");
        expected.add("getId");

        assertEquals(3, getters.length);
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void getClassGettersForFieldsTest() {
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("lastName");
        fields.add("id");

        Method[] gettersForFields = getClassGettersForFields(TestClass.class, fields);
        List<String> actual = stream(gettersForFields).map(Method::getName).collect(toList());

        List<String> expected = new ArrayList<>();
        expected.add("getName");
        expected.add("getLastName");
        expected.add("getId");

        assertEquals(3, gettersForFields.length);
        assertEquals(expected, actual);
    }

    @Test(expected = GetterNotFoundInClassException.class)
    public void getClassGettersForFieldsWithFieldNotPresentTest() {
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("lastName");
        fields.add("notPresent");

        getClassGettersForFields(TestClass.class, fields);
    }
}
