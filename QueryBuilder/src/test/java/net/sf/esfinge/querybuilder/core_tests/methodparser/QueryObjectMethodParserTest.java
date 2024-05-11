package net.sf.esfinge.querybuilder.core_tests.methodparser;

import java.util.List;
import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.Location;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Contains;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.QueryObject;
import net.sf.esfinge.querybuilder.core_tests.utils.AssertException;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyException;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import net.sf.esfinge.querybuilder.exception.QueryObjectException;
import net.sf.esfinge.querybuilder.exception.WrongParamNumberException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.MethodParser;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryObjectMethodParser;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.jmock.Expectations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class QueryObjectMethodParserTest extends MethodParserTest {

    protected ClassMock queryMockClass;
    protected Class queryClass;

    @Before
    public void createQueryObject() {
        queryMockClass = new ClassMock("QueryClass");
    }

    @Override
    protected MethodParser createParserClass() {
        return new QueryObjectMethodParser();
    }

    @Test
    public void doNotVerifyMatchingWithoutParams() throws Exception {
        var m = createMethodForTesting(List.class, "getPerson");
        var match = parser.fitParserConvention(m);
        assertFalse("Should not match", match);
    }

    @Test
    public void doNotVerifyMatchingWithtParam() throws Exception {
        var m = createMethodForTesting(List.class, "getPerson", String.class);
        var match = parser.fitParserConvention(m);
        assertFalse("Should not match", match);
    }

    @Test
    public void verifyMatchingWithBySomething() throws Exception {
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(List.class, "getPersonByName", QueryObject.class, queryClass);
        var match = parser.fitParserConvention(m);
        assertFalse("Should not match", match);
    }

    @Test
    public void verifyMatching() throws Exception {
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(List.class, "getPerson", QueryObject.class, queryClass);
        var match = parser.fitParserConvention(m);
        assertTrue("Should match", match);
    }

    @Test
    public void getListEntityName() throws Exception {
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(List.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        assertEquals("Entity should be Person", "Person", qi.getEntityName());
        assertEquals("Entity should be Person", Person.class, qi.getEntityType());
        assertEquals("The query type should be RETRIEVE_LIST", QueryType.RETRIEVE_LIST, qi.getQueryType());
    }

    @Test
    public void getSingleEntityName() throws Exception {
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        assertEquals("The query type should be RETRIEVE_SINGLE", QueryType.RETRIEVE_SINGLE, qi.getQueryType());
    }

    @Test
    public void queryWithOneRegularProperty() throws Exception {
        queryMockClass.addProperty("name", String.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var sequence = context.sequence("query sequence");
                one(visitorMock).visitEntity("Person");
                inSequence(sequence);
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                inSequence(sequence);
                one(visitorMock).visitEnd();
                inSequence(sequence);
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithOneRegularMethod() throws Exception {
        queryMockClass.addMethod(String.class, "getName");
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var sequence = context.sequence("query sequence");
                one(visitorMock).visitEntity("Person");
                inSequence(sequence);
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                inSequence(sequence);
                one(visitorMock).visitEnd();
                inSequence(sequence);
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithNoExistingProperty() throws Exception {
        queryMockClass.addProperty("unknown", String.class);
        queryClass = queryMockClass.createClass();
        final var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        new AssertException(QueryObjectException.class, InvalidPropertyException.class) {
            @Override
            protected void run() {
                var qi = parser.parse(m);
            }
        };
    }

    @Test
    public void queryWithNoExistingMethod() throws Exception {
        queryMockClass.addMethod(String.class, "getUnknown");
        queryClass = queryMockClass.createClass();
        final var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        new AssertException(QueryObjectException.class, InvalidPropertyException.class) {
            @Override
            protected void run() {
                var qi = parser.parse(m);
            }
        };
    }

    @Test
    public void queryWithWrongTypeProperty() throws Exception {
        queryMockClass.addProperty("name", int.class);
        queryClass = queryMockClass.createClass();
        final var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        new AssertException(QueryObjectException.class, InvalidPropertyTypeException.class) {
            @Override
            protected void run() {
                var qi = parser.parse(m);
            }
        };
    }

    @Test
    public void queryWithWrongTypeMerhod() throws Exception {
        queryMockClass.addMethod(int.class, "getName");
        queryClass = queryMockClass.createClass();
        final var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        new AssertException(QueryObjectException.class, InvalidPropertyTypeException.class) {
            @Override
            protected void run() {
                var qi = parser.parse(m);
            }
        };
    }

    @Test
    public void queryWithMoreThanOneProperty() throws Exception {
        queryMockClass.addProperty("name", String.class);
        queryMockClass.addProperty("lastName", String.class);
        queryMockClass.addProperty("age", int.class);
        queryMockClass.addProperty("addressCity", String.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.city", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithPropertiesAndMethods() throws Exception {
        queryMockClass.addMethod(String.class, "getName");
        queryMockClass.addMethod(String.class, "getLastName");
        queryMockClass.addProperty("age", int.class);
        queryMockClass.addProperty("addressCity", String.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.city", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithComparisonTypesOnProperties() throws Exception {
        queryMockClass.addProperty("nameContains", String.class);
        queryMockClass.addProperty("ageGreater", int.class);
        queryMockClass.addProperty("addressCityStarts", String.class);
        queryMockClass.addProperty("addressNumberLesserOrEquals", int.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.GREATER);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.city", ComparisonType.STARTS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.number", ComparisonType.LESSER_OR_EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithComparisonTypesOnMethods() throws Exception {
        queryMockClass.addMethod(String.class, "getNameContains");
        queryMockClass.addMethod(int.class, "getAgeGreater");
        queryMockClass.addMethod(String.class, "getAddressCityStarts");
        queryMockClass.addMethod(int.class, "getAddressNumberLesserOrEquals");
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.GREATER);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.city", ComparisonType.STARTS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.number", ComparisonType.LESSER_OR_EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithComparisonTypesUsingAnnotations() throws Exception {
        queryMockClass.addProperty("name", String.class);
        queryMockClass.addAnnotation("name", Contains.class, Location.FIELD);
        queryMockClass.addProperty("addressNumber", int.class);
        queryMockClass.addAnnotation("addressNumber", Greater.class, Location.GETTER);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.number", ComparisonType.GREATER);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithComparisonTypesUsingAnnotationsOnMethods() throws Exception {
        queryMockClass.addMethod(String.class, "getName");
        queryMockClass.addMethodAnnotation("getName", Contains.class);
        queryMockClass.addMethod(int.class, "getAddressNumber");
        queryMockClass.addMethodAnnotation("getAddressNumber", Greater.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("address.number", ComparisonType.GREATER);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithNullOptions() throws Exception {
        queryMockClass.addProperty("name", String.class);
        queryMockClass.addAnnotation("name", IgnoreWhenNull.class, Location.FIELD);
        queryMockClass.addProperty("lastNameStarts", String.class);
        queryMockClass.addAnnotation("lastNameStarts", CompareToNull.class, Location.GETTER);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("lastName", ComparisonType.STARTS, NullOption.COMPARE_TO_NULL);
                when(s.is("condicao"));
                then(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithOrderBy() throws Exception {
        queryMockClass.addProperty("name", String.class);
        queryMockClass.addProperty("age", int.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPersonOrderByLastNameDesc", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitOrderBy("lastName", OrderingDirection.DESC);
                when(s.is("conector"));
                then(s.is("final"));
                one(visitorMock).visitEnd();
                when(s.is("final"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryMethodsWithOrderBy() throws Exception {
        queryMockClass.addMethod(String.class, "getName");
        queryMockClass.addMethod(int.class, "age");
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPersonOrderByLastNameDesc", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitOrderBy("lastName", OrderingDirection.DESC);
                when(s.is("conector"));
                then(s.is("final"));
                one(visitorMock).visitEnd();
                when(s.is("final"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithDomainTerm() throws Exception {
        var an = domainTermWithOneCondition("underage", "age", "18", ComparisonType.LESSER);
        mockClass.addAnnotation(an);
        queryMockClass.addProperty("name", String.class);
        queryMockClass.addProperty("lastNameStarts", String.class);
        queryClass = queryMockClass.createClass();
        var m = createMethodWithAnnotationForTesting(Person.class, "getPersonUnderage", QueryObject.class, queryClass);
        var qi = parser.parse(m);
        final var visitorMock = context.mock(QueryVisitor.class);

        context.checking(new Expectations() {
            {
                var s = context.states("QUERY").startsAs("inicio");
                one(visitorMock).visitEntity("Person");
                when(s.is("inicio"));
                then(s.is("condicao"));
                one(visitorMock).visitCondition("name", ComparisonType.EQUALS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("lastName", ComparisonType.STARTS);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitCondition("age", ComparisonType.LESSER, 18);
                when(s.is("condicao"));
                then(s.is("conector"));
                one(visitorMock).visitEnd();
                when(s.is("conector"));
                allowing(visitorMock).visitConector("and");
                when(s.is("conector"));
                then(s.is("condicao"));
            }
        });

        qi.visit(visitorMock);
    }

    @Test
    public void queryWithMoreThanOneParameter() throws Exception {
        queryMockClass.addMethod(Person.class, "getPerson");
        queryClass = queryMockClass.createClass();

        mockClass.addAbstractMethod(queryClass, "getPerson", queryClass, Boolean.class);
        mockClass.addMethodParamAnnotation(0, "getPerson", QueryObject.class);

        Class<?> c = mockClass.createClass();
        parser = createParserClass();
        parser.setInterface(c);

        parser.setEntityClassProvider(classProviderMock);
        final var m = c.getMethod("getPerson", queryClass, Boolean.class);

        new AssertException(WrongParamNumberException.class, "The method getPerson is using @QueryObject annotation but have more than one parameter") {
            @Override
            protected void run() {
                parser.parse(m);
            }
        };
    }

}
