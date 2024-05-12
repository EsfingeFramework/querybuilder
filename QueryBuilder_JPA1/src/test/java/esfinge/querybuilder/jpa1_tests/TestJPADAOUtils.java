package esfinge.querybuilder.jpa1_tests;

import esfinge.querybuilder.jpa1.JPADAOUtils;
import java.lang.reflect.Method;
import javax.persistence.Transient;
import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.Location;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestJPADAOUtils {

    private ClassMock mockClass;
    private Class clazz;

    @Before
    public void createMockClass() {
        mockClass = new ClassMock("ExampleClass");
        mockClass.addProperty("prop", String.class);
    }

    @Test
    public void testIsGetter() throws Exception {
        clazz = mockClass.createClass();
        Method m = clazz.getMethod("getProp");
        assertTrue(JPADAOUtils.isGetter(m));
    }

    @Test
    public void testIsNotGetter() throws Exception {
        clazz = mockClass.createClass();
        Method m = clazz.getMethod("setProp", String.class);
        assertFalse(JPADAOUtils.isGetter(m));
    }

    @Test
    public void testIsNotTransientGetter() throws Exception {
        clazz = mockClass.createClass();
        Method m = clazz.getMethod("getProp");
        assertTrue(JPADAOUtils.isGetterWhichIsNotTransient(m, clazz));
    }

    @Test
    public void testIsTransientGetterWithGetterAnnotation() throws Exception {
        mockClass.addAnnotation("prop", Transient.class, Location.GETTER);
        clazz = mockClass.createClass();
        Method m = clazz.getMethod("getProp");
        assertFalse(JPADAOUtils.isGetterWhichIsNotTransient(m, clazz));
    }

    @Test
    public void testIsTransientGetterWithFieldAnnotation() throws Exception {
        mockClass.addAnnotation("prop", Transient.class, Location.FIELD);
        clazz = mockClass.createClass();
        Method m = clazz.getMethod("getProp");
        assertFalse(JPADAOUtils.isGetterWhichIsNotTransient(m, clazz));
    }

}
