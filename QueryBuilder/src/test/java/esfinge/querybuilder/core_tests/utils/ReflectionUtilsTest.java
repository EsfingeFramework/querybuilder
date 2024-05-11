package esfinge.querybuilder.core_tests.utils;

import java.util.Date;
import esfinge.querybuilder.core_tests.methodparser.Person;
import esfinge.querybuilder.core.exception.InvalidPropertyException;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void simplePropertyType() {
        var type = ReflectionUtils.getPropertyType(Person.class, "name");
        assertEquals("The name property is a String", String.class, type);
    }

    @Test
    public void booleanPropertyType() {
        var type = ReflectionUtils.getPropertyType(Person.class, "authorized");
        assertEquals("The authorized property is a boolean", boolean.class, type);
    }

    @Test(expected = InvalidPropertyException.class)
    public void getTypeOfNonExistingProperty() {
        ReflectionUtils.getPropertyType(Person.class, "noExisting");
    }

    @Test
    public void compositePropertyType() {
        var type = ReflectionUtils.getPropertyType(Person.class, "address.number");
        assertEquals("The number property inside property address is int", int.class, type);
    }

    @Test
    public void existingProperty() {
        var exists = ReflectionUtils.existProperty(Person.class, "address");
        assertTrue("The property address exists", exists);
    }

    @Test
    public void nonExistingProperty() {
        var exists = ReflectionUtils.existProperty(Person.class, "other");
        assertFalse("The property other do not exist", exists);
    }

    @Test
    public void parameterNameWithoutAnything() throws SecurityException, NoSuchMethodException {
        var m = ExampleQueryClass.class.getMethod("getName");
        var s = ReflectionUtils.getQueryParamenterName(m);
        assertEquals("nameEquals", s);
    }

    @Test
    public void parameterNameWithFieldAnnotation() throws SecurityException, NoSuchMethodException {
        var m = ExampleQueryClass.class.getMethod("getAge");
        var s = ReflectionUtils.getQueryParamenterName(m);
        assertEquals("ageLesser", s);
    }

    @Test
    public void parameterNameWithGetterAnnotation() throws SecurityException, NoSuchMethodException {
        var m = ExampleQueryClass.class.getMethod("getLastName");
        var s = ReflectionUtils.getQueryParamenterName(m);
        assertEquals("lastNameContains", s);
    }

    @Test
    public void parameterNameWithConvention() throws SecurityException, NoSuchMethodException {
        var m = ExampleQueryClass.class.getMethod("getBirthDateGreater");
        var s = ReflectionUtils.getQueryParamenterName(m);
        assertEquals("birthDateGreater", s);
    }

    @Test
    public void createParameterMap() {
        var date = new Date();
        var ex = new ExampleQueryClass();
        ex.setName("john");
        ex.setAge(20);
        ex.setBirthDateGreater(date);
        ex.setLastName("connor");

        var map = ReflectionUtils.toParameterMap(ex);

        assertEquals(4, map.size());
        assertEquals("john", map.get("nameEquals"));
        assertEquals(20, map.get("ageLesser"));
        assertEquals(date, map.get("birthDateGreater"));
        assertEquals("connor", map.get("lastNameContains"));
    }

}
