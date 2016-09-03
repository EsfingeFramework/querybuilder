package net.sf.esfinge.querybuilder.neo4j.oomapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import net.sf.esfinge.querybuilder.neo4j.oomapper.parser.Parser;
import net.sf.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.UnindexedPropertyException;
import net.sf.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.UnmappedPropertyException;

public class Query <E> {
	
	private HashMap<String, ArrayList<Object>> propertyValue = new HashMap<String, ArrayList<Object>>();
	private Condition condition;
	private Neo4J neo;
	private MappingInfo info;
	private String defaultPropertyConector = " AND ";
	private String[] sortFields;
	
	public Query(Neo4J neo, MappingInfo info){
		this.neo = neo;
		this.info = info;
	}
	
	public void setDefaultConnectorAND(){
		defaultPropertyConector = " AND ";
	}
	
	public void setDefaultConnectorOR(){
		defaultPropertyConector = " OR ";
	}

	public Query<E> setProperty(String propertyName, Object value) {
		
		if(!info.isMappedProperty(propertyName))
			throw new UnmappedPropertyException("Property " + propertyName + " of NodeEntity Class " + info.getClazz().getName());
		
		if(!info.isIndexedProperty(propertyName))
			throw new UnindexedPropertyException("Property " + propertyName + " of NodeEntity Class " + info.getClazz().getName());
		
		if(propertyValue.containsKey(propertyName))
			propertyValue.get(propertyName).add(value);
		else{
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(value);
			propertyValue.put(propertyName, values);
		}
		return this;
	}

	public Condition or(Condition... conditions){
		condition = new Condition(ConditionType.OR, conditions);
		return condition;
	}
	
	public Condition or(List<Condition> conditions){
		condition = new Condition(ConditionType.OR, conditions);
		return condition;
	}
	
	public Condition and(Condition... conditions){
		condition = new Condition(ConditionType.AND, conditions);
		return condition;
	}
	
	public Condition and(List<Condition> conditions){
		condition = new Condition(ConditionType.AND, conditions);
		return condition;
	}
	
	public void orderBy(String... sortFields){
		this.sortFields = sortFields;	
	}
	
	public String toString(){
		
		if(condition == null){
			String luceneQuery = "";
			boolean first = true;
			for(String property : propertyValue.keySet()){
				ArrayList<Object> values = propertyValue.get(property);
				if(!first)
					luceneQuery += defaultPropertyConector;
				first = false;
				luceneQuery += "(" + property + ":\"" + values.get(0) + "\"";
				for(int i = 1; i < values.size(); i++)
					luceneQuery += " OR " + property + ":\"" + values.get(i) + "\"";
				luceneQuery += ")";
			}
			if(first){
				luceneQuery += info.getId() + ":*";  //Treats queries with no conditions
			}
			return luceneQuery;
		}
		else{
			condition.verifyIndexedProperties(info);
			return condition.toString();
		}
		
	}
	
	public E getSingle(){
		List<E> results = asList();
		if (results.size() == 0)
			return null;
		return results.get(0);
	}
	
	public List<E> asList(){
		
		ArrayList<E> output = new ArrayList<E>();
		
		/*
		 * 		List<Object> collect = neo.getIndex(info.getClazz()).query(toString()).stream().collect(Collectors.toList());
		
		for (Object object : collect) {
			Node i = (Node) object;

		 */
		
		Transaction t = neo.beginTx();
		Iterator<Node> i = neo.getIndex(info.getClazz()).query(toString()).iterator();
		
		try {
			while(i.hasNext()){
				Parser parser = neo.getParser();
				E entity;
					entity = parser.<E>getEntity(i.next(), info);
					output.add(entity);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException e) {
			neo.failureTx(t);
			e.printStackTrace();
		} finally {
			neo.successTx(t);
		}
		
		if(sortFields != null){
		
			Collections.sort(output, new Comparator<E>() {
	
				@Override
				public int compare(E arg0, E arg1) {
					
					for(String sortingField : sortFields){
						
						int desc = 1;
						
						if(sortingField.startsWith("-")){
							desc = -1;
							sortingField = sortingField.substring(1);
						}
						
						for(Method m : arg0.getClass().getDeclaredMethods()){
							
							if(m.getName().equalsIgnoreCase("get" + sortingField)){
								
								if(m.getReturnType() == String.class){
									String field0 = null;
									String field1 = null;
									try {
										field0 = (String) m.invoke(arg0);
										field1 = (String) m.invoke(arg1);
									} catch (IllegalAccessException
											| IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
									}
									
									if(field0.compareTo(field1) != 0)
										return desc*field0.compareTo(field1);
								}
								else if (Number.class.isAssignableFrom(m.getReturnType())){
									Number field0 = null;
									Number field1 = null;
									try {
										field0 = (Number) m.invoke(arg0);
										field1 = (Number) m.invoke(arg1);
									} catch (IllegalAccessException
											| IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
									}
									if(field0.intValue() - field1.intValue() != 0)
										return desc*field0.intValue() - field1.intValue();
								}
									
							}
								
						}
						
					}
					return 0;
				}
			});
		
		}	
		
		return output;
	}
}
