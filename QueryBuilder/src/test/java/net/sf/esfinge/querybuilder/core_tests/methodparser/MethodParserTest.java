package net.sf.esfinge.querybuilder.core_tests.methodparser;

import java.lang.reflect.Method;
import net.sf.esfinge.classmock.Annotation;
import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.methodparser.MethodParser;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public abstract class MethodParserTest {

    protected ClassMock mockClass;
    protected MethodParser parser;
    protected EntityClassProvider classProviderMock;

    protected Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Before
    public void createMethodParser() {
        classProviderMock = context.mock(EntityClassProvider.class);

        context.checking(new Expectations() {
            {
                allowing(classProviderMock).getEntityClass("Person");
                will(returnValue(Person.class));
                allowing(classProviderMock).getEntityClass("JuridicPerson");
                will(returnValue(Person.class));
                allowing(classProviderMock).getEntityClass(with(any(String.class)));
                will(returnValue(null));
            }
        });
    }

    @Before
    public void createMockClass() {
        mockClass = new ClassMock("ExampleInterface", true);
    }

    protected Method createMethodForTesting(Class<?> returnType, String methodName,
            Class<?>... paramTypes) throws NoSuchMethodException {
        mockClass.addAbstractMethod(returnType, methodName, paramTypes);
        Class<?> c = mockClass.createClass();
        parser = createParserClass();
        parser.setInterface(c);
        parser.setEntityClassProvider(classProviderMock);
        return c.getMethod(methodName, paramTypes);
    }

    protected abstract MethodParser createParserClass();

    protected Method createMethodWithAnnotationForTesting(Class<?> returnType,
            String methodName, Class<?> annotation, Class<?>... paramTypes) throws NoSuchMethodException {
        mockClass.addAbstractMethod(returnType, methodName, paramTypes);
        mockClass.addMethodParamAnnotation(0, methodName, annotation);
        Class<?> c = mockClass.createClass();
        parser = createParserClass();
        parser.setInterface(c);
        parser.setEntityClassProvider(classProviderMock);
        return c.getMethod(methodName, paramTypes);
    }

    @Test(expected = EntityClassNotFoundException.class)
    public void entityClassNotFound() throws Exception {
        var m = createMethodForTesting(Object.class, "getUnknownEntity");
        parser.parse(m);
    }

    protected Annotation createDomainTerm(String term) {
        var dt = new Annotation(DomainTerm.class);
        dt.addProperty("term", term);
        return dt;
    }

    protected Annotation createCondition(String property, Object value, ComparisonType comp) {
        var c = new Annotation(Condition.class);
        c.addProperty("property", property);
        c.addProperty("value", value);
        c.addProperty("comparison", comp);
        return c;
    }

    protected Annotation domainTermWithOneCondition(String term, String property,
            Object value, ComparisonType comp) {
        var t = createDomainTerm(term);
        var c = createCondition(property, value, comp);
        t.addProperty("conditions", new Annotation[]{c});
        return t;
    }

}
