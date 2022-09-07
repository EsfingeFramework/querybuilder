package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryUtils;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass1;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecondaryQueryUtilsTest {

    @Test
    public void twoEqualObjectsShouldBeEqualTest() {
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(1, "Pedro", "Silva");

        assertTrue(SecondaryQueryUtils.reflectiveEquals(obj1, obj2));
    }

    @Test
    public void twoDifferentObjectsShouldNotBeEqualTest() {
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(1, "Pedros", "Silva");

        assertFalse(SecondaryQueryUtils.reflectiveEquals(obj1, obj2));
    }

    @Test
    public void twoObjectsWithDifferentClassShouldNotBeEqualTest() {
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass1 obj2 = new TestClass1(1, "Pedro", "Silva");

        assertFalse(SecondaryQueryUtils.reflectiveEquals(obj1, obj2));
    }

}
