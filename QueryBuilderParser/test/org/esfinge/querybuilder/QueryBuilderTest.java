package org.esfinge.querybuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.esfinge.querybuilder.executor.QueryExecutor;
import org.esfinge.querybuilder.methodparser.DSLMethodParser;
import org.esfinge.querybuilder.methodparser.Person;
import org.esfinge.querybuilder.methodparser.QueryInfo;
import org.esfinge.querybuilder.methodparser.SelectorMethodParser;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class QueryBuilderTest {
	
	protected Mockery context = new Mockery() {
		{ setImposteriser(ClassImposteriser.INSTANCE); }
	};
	
	public interface TestQueries extends Repository<Person> {
		public Person getPersonByName(String param);
	}
	
	@After
	public void removeConfigurations() {
		QueryBuilder.configureMethodParser(null);
		QueryBuilder.configureQueryExecutor(null);
		QueryBuilder.clearCache();
	}
	
	@Test
	public void createBasicProxy() {
		Object obj = QueryBuilder.create(TestQueries.class);
		assertTrue("The return should be assignable to the interface", obj instanceof TestQueries);
	}
	
	@Test
	public void executeProxyMethod() throws Exception {
		final DSLMethodParser mp = context.mock(DSLMethodParser.class);
		final QueryExecutor qe = context.mock(QueryExecutor.class);
		
		QueryBuilder.clearQueryInfoCache();
		QueryBuilder.configureMethodParser(mp);
		QueryBuilder.configureQueryExecutor(qe);
		
		TestQueries tq = QueryBuilder.create(TestQueries.class);
		
		final Method m = TestQueries.class.getMethod("getPersonByName", String.class);
		final QueryInfo qi = new QueryInfo();
		
		context.checking(new Expectations() {
			{
				one(mp).parse(m);
				will(returnValue(qi));
				one(qe).executeQuery(qi, new Object[]{"Pedro"});
				will(returnValue(new Person("Pedro")));
			}
		});
		
		Person p = tq.getPersonByName("Pedro");
		assertEquals("The name should be the same returned by QueryExecutor", "Pedro", p.getName());
	}

	@Test
	public void cachedQueryInfo() throws Exception {
		final DSLMethodParser mp = context.mock(DSLMethodParser.class);
		final QueryExecutor qe = context.mock(QueryExecutor.class);
		
		QueryBuilder.clearQueryInfoCache();
		QueryBuilder.configureMethodParser(mp);
		QueryBuilder.configureQueryExecutor(qe);
		
		TestQueries tq = QueryBuilder.create(TestQueries.class);
		
		final Method m = TestQueries.class.getMethod("getPersonByName", String.class);
		final QueryInfo qi = new QueryInfo();
		
		context.checking(new Expectations() {
			{
				one(mp).parse(m);
				will(returnValue(qi));
				one(qe).executeQuery(qi, new Object[]{"Pedro"});
				will(returnValue(new Person("Pedro")));
				one(qe).executeQuery(qi, new Object[]{"João"});
				will(returnValue(new Person("João")));
			}
		});
		
		Person p1 = tq.getPersonByName("Pedro");
		assertEquals("The name should be the same returned by QueryExecutor", "Pedro", p1.getName());
		Person p2 = tq.getPersonByName("João");
		assertEquals("The name should be the same returned by QueryExecutor", "João", p2.getName());
	}
	
	@Test
	public void cachedProxyInstance() throws Exception {
		TestQueries tq1 = QueryBuilder.create(TestQueries.class);
		TestQueries tq2 = QueryBuilder.create(TestQueries.class);
		
		assertTrue("The returned object should be the same", tq1 == tq2);
	}
	
	@Test
	public void serviceLoaderRetrieval() throws Exception {
		QueryExecutor qe = QueryBuilder.getConfiguredQueryExecutor();
		SelectorMethodParser mp = (SelectorMethodParser) QueryBuilder.getConfiguredMethodParser(this.getClass());
				
		assertTrue("The query executor should be of the configured class", qe instanceof DummyQueryExecutor);
		assertTrue("The entity locator should be of the configured class", mp.getEntityClassProvider() instanceof DummyEntityClassProvider);
	}
	
	@Test
	public void invokeRepositoryMethods() {
		TestQueries tq = QueryBuilder.create(TestQueries.class);
		tq.save(new Person());
		assertEquals("save", DummyRepository.instance.getLastMethodCalled());
		tq.delete(1);
		assertEquals("delete", DummyRepository.instance.getLastMethodCalled());
		tq.list();
		assertEquals("list", DummyRepository.instance.getLastMethodCalled());
		tq.getById(1);
		assertEquals("getById", DummyRepository.instance.getLastMethodCalled());
		tq.queryByExample(new Person());
		assertEquals("queryByExample", DummyRepository.instance.getLastMethodCalled());
	}

}
