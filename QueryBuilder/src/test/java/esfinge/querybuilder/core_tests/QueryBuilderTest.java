//package esfinge.querybuilder.core_tests;
//
//import esfinge.querybuilder.core.QueryBuilder;
//import esfinge.querybuilder.core.Repository;
//import esfinge.querybuilder.core.annotation.TargetEntity;
//import esfinge.querybuilder.core.executor.QueryExecutor;
//import esfinge.querybuilder.core.methodparser.DSLMethodParser;
//import esfinge.querybuilder.core.methodparser.QueryInfo;
//import esfinge.querybuilder.core.methodparser.SelectorMethodParser;
//import esfinge.querybuilder.core_tests.methodparser.Person;
//import java.util.HashMap;
//import java.util.Map;
//import org.jmock.Expectations;
//import org.jmock.Mockery;
//import org.jmock.integration.junit4.JMock;
//import org.jmock.lib.legacy.ClassImposteriser;
//import org.junit.After;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(JMock.class)
//
//public class QueryBuilderTest {
//
//    protected Mockery context = new Mockery() {
//        {
//            setImposteriser(ClassImposteriser.INSTANCE);
//        }
//    };
//
//    @TargetEntity(Person.class)
//    public interface TestQueries extends Repository<Person> {
//
//        public Person getPersonByName(String param);
//    }
//
//    @After
//    public void removeConfigurations() {
//        QueryBuilder.configureMethodParser(null);
//        QueryBuilder.configureQueryExecutors(null);
//        QueryBuilder.clearCache();
//    }
//
//    @Test
//    public void createBasicProxy() {
//        Object obj = QueryBuilder.create(TestQueries.class);
//        assertTrue("The return should be assignable to the interface", obj instanceof TestQueries);
//    }
//
//    @Test
//    public void executeProxyMethod() throws Exception {
//        final var mp = context.mock(DSLMethodParser.class);
//        final var qe = context.mock(QueryExecutor.class);
//
//        QueryBuilder.clearQueryInfoCache();
//        QueryBuilder.configureMethodParser(mp);
//        Map map = new HashMap<>();
//        map.put("Dummy", qe);
//        QueryBuilder.configureQueryExecutors(map);
//        var tq = QueryBuilder.create(TestQueries.class);
//        final var m = TestQueries.class.getMethod("getPersonByName", String.class);
//        final var qi = new QueryInfo();
//
//        context.checking(new Expectations() {
//            {
//                one(mp).parse(m);
//                will(returnValue(qi));
//                one(qe).executeQuery(qi, new Object[]{"Pedro"});
//                will(returnValue(new Person("Pedro")));
//            }
//        });
//
//        var p = tq.getPersonByName("Pedro");
//        assertEquals("The name should be the same returned by QueryExecutor", "Pedro", p.getName());
//    }
//
//    @Test
//    public void cachedQueryInfo() throws Exception {
//
//        final var mp = context.mock(DSLMethodParser.class);
//        final var qe = context.mock(QueryExecutor.class);
//
//        QueryBuilder.clearQueryInfoCache();
//        QueryBuilder.configureMethodParser(mp);
//        Map map = new HashMap<>();
//        map.put("Dummy", qe);
//        QueryBuilder.configureQueryExecutors(map);
//
//        var tq = QueryBuilder.create(TestQueries.class);
//        final var m = TestQueries.class.getMethod("getPersonByName", String.class);
//        final var qi = new QueryInfo();
//
//        context.checking(new Expectations() {
//            {
//                one(mp).parse(m);
//                will(returnValue(qi));
//                one(qe).executeQuery(qi, new Object[]{"Pedro"});
//                will(returnValue(new Person("Pedro")));
//                one(qe).executeQuery(qi, new Object[]{"João"});
//                will(returnValue(new Person("João")));
//            }
//        });
//
//        var p1 = tq.getPersonByName("Pedro");
//        assertEquals("The name should be the same returned by QueryExecutor", "Pedro", p1.getName());
//        var p2 = tq.getPersonByName("João");
//        assertEquals("The name should be the same returned by QueryExecutor", "João", p2.getName());
//    }
//
//    @Test
//    public void cachedProxyInstance() throws Exception {
//        var tq1 = QueryBuilder.create(TestQueries.class);
//        var tq2 = QueryBuilder.create(TestQueries.class);
//        assertTrue("The returned object should be the same", tq1 == tq2);
//    }
//
//    @Test
//    public void serviceLoaderRetrieval() throws Exception {
//        var qe = QueryBuilder.getConfiguredQueryExecutor("Dummy");
//        var mp = (SelectorMethodParser) QueryBuilder.getConfiguredMethodParser(this.getClass());
//
//        assertTrue("The query executor should be of the configured class", qe instanceof DummyQueryExecutor);
//        assertTrue("The entity locator should be of the configured class", mp.getEntityClassProvider() instanceof DummyEntityClassProvider);
//    }
//
//    @Test
//    public void invokeRepositoryMethods() {
//        var tq = QueryBuilder.create(TestQueries.class);
//        tq.save(new Person());
//        assertEquals("save", DummyRepository.instance.getLastMethodCalled());
//        tq.delete(1);
//        assertEquals("delete", DummyRepository.instance.getLastMethodCalled());
//        tq.list();
//        assertEquals("list", DummyRepository.instance.getLastMethodCalled());
//        tq.getById(1);
//        assertEquals("getById", DummyRepository.instance.getLastMethodCalled());
//        tq.queryByExample(new Person());
//        assertEquals("queryByExample", DummyRepository.instance.getLastMethodCalled());
//    }
//}
