package ef.qb.core_tests.methodparser;

import ef.qb.core.annotation.Condition;
import ef.qb.core.annotation.DomainTerm;
import ef.qb.core.annotation.TargetEntity;
import ef.qb.core.exception.EntityClassNotFoundException;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.MethodParser;
import ef.qb.core_tests.EntityClassProvider;
import java.lang.reflect.Method;
import net.sf.esfinge.classmock.Annotation;
import net.sf.esfinge.classmock.ClassMock;
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
        return createMethodForTesting(returnType, "Person", methodName, paramTypes);
    }

    protected Method createMethodForTesting(Class<?> returnType, String classMockName, String methodName,
            Class<?>... paramTypes) throws NoSuchMethodException {
        mockClass.addAbstractMethod(returnType, methodName, paramTypes);
        var entityClass = classProviderMock.getEntityClass(classMockName);
        if (entityClass != null) {
            mockClass.addAnnotation(TargetEntity.class, "value", entityClass);
        }
        Class<?> c = mockClass.createClass();
        parser = createParserClass();
        parser.setInterface(c);
        return c.getMethod(methodName, paramTypes);
    }

    protected abstract MethodParser createParserClass();

    protected Method createMethodWithAnnotationForTesting(Class<?> returnType,
            String methodName, Class<?> annotation, Class<?>... paramTypes) throws NoSuchMethodException {
        mockClass.addAbstractMethod(returnType, methodName, paramTypes);
        mockClass.addMethodParamAnnotation(0, methodName, annotation);
        mockClass.addAnnotation(TargetEntity.class, "value", classProviderMock.getEntityClass("Person"));
        Class<?> c = mockClass.createClass();
        parser = createParserClass();
        parser.setInterface(c);
        return c.getMethod(methodName, paramTypes);
    }

    @Test(expected = EntityClassNotFoundException.class)
    public void entityClassNotFound() throws Exception {
        var m = createMethodForTesting(Object.class, null, "getUnknownEntity");
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
