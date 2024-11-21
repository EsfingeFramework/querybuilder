package ef.qb.cassandra_tests.unit.querybuilding;

import static ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryUtils.reflectiveEquals;
import ef.qb.cassandra_tests.unit.reflection.TestClass;
import ef.qb.cassandra_tests.unit.reflection.TestClass1;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecondaryQueryUtilsTest {

    @Test
    public void twoEqualObjectsShouldBeEqualTest() {
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(1, "Pedro", "Silva");

        assertTrue(reflectiveEquals(obj1, obj2));
    }

    @Test
    public void twoDifferentObjectsShouldNotBeEqualTest() {
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(1, "Pedros", "Silva");

        assertFalse(reflectiveEquals(obj1, obj2));
    }

    @Test
    public void twoObjectsWithDifferentClassShouldNotBeEqualTest() {
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass1 obj2 = new TestClass1(1, "Pedro", "Silva");

        assertFalse(reflectiveEquals(obj1, obj2));
    }

}
