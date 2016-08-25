package org.esfinge.querybuilder.neo4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.esfinge.querybuilder.neo4j.oomapper.Condition;
import org.esfinge.querybuilder.neo4j.oomapper.ConditionType;
import org.esfinge.querybuilder.neo4j.oomapper.Neo4J;
import org.esfinge.querybuilder.neo4j.oomapper.Query;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.RelatedTo;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.IndexHits;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class ConditionDescription {
	
	private String propertyName;
	private String paramName;
	private ComparisonType compType;
	private Object fixedValue;
	private NullOption nullOption = NullOption.NONE;
	private String nextConector = "";
	
	public ConditionDescription(String propertyName,	ComparisonType compType) {
		super();
		this.propertyName = propertyName;
		this.paramName = propertyName.substring(propertyName.lastIndexOf(".")+1);
		this.compType = compType;
	}
	
	public NullOption getNullOption() {
		return nullOption;
	}
	public void setNullOption(NullOption nullOption) {
		this.nullOption = nullOption;
	}
	public String getNextConector() {
		return nextConector;
	}
	public void setNextConector(String nextConector) {
		this.nextConector = nextConector;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public String getParamName() {
		return paramName;
	}
	public ComparisonType getCompType() {
		return compType;
	}
	public String getCondition(){
		throw new UnsupportedOperationException();
	}

	public Object getFixedValue() {
		return fixedValue;
	}
	public void setFixedValue(Object fixedValue) {
		paramName += compType.name();
		this.fixedValue = fixedValue;
	}
	
	public void addCondition(ArrayList<ArrayList<Condition>> criteria, Class<?> clazz, ParameterFormater formatter) {
		addCondition(criteria, formatter.formatParameter(fixedValue), clazz);
	}
	
	public void addCondition(ArrayList<ArrayList<Condition>> criteria, Object value, Class<?> clazz) {
		
		if(!(value == null && nullOption == NullOption.IGNORE_WHEN_NULL)){
		
			Condition newCriteria;
			boolean composite = false;
			
			if(propertyName.indexOf(".") != -1){
				for(Field field : clazz.getDeclaredFields()){
					if(field.getName().equalsIgnoreCase(propertyName.substring(0, propertyName.indexOf(".")))){
						if(field.isAnnotationPresent(RelatedTo.class))
							composite = true;
					}
				}
			}
			
			if(value != null){
				
				if(composite)
					newCriteria = compositeProperty(clazz, value);
				else
					newCriteria = getQuery(clazz).or(new Condition(propertyName, value, compType));
					
			}
			else{
				
				if(composite)
					newCriteria = getQuery(clazz).or(new Condition(propertyName.substring(0, propertyName.indexOf(".")), "null"));
				else
					newCriteria = getQuery(clazz).or(new Condition(propertyName, "null"));
			
			}
			criteria.get(criteria.size() - 1).add(newCriteria);
			
			if(nextConector.equals("and")){
				//Do nothing
			}else if(nextConector.equals("or")){
				criteria.add(new ArrayList<Condition>());
			}else if(nextConector.equals("")){
				//Do nothing
			}else{
				System.err.println("Unsupported connector!");
			}
		}
	}
	
	private Condition compositeProperty(Class<?> clazz, Object value){
		
		EntityClassProvider ecp = ServiceLocator.getServiceImplementation(EntityClassProvider.class);
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		Neo4J neo = dsp.getDatastore();
		
		String[] properties = propertyName.split("\\.");
		
		IndexHits<Node> nodes = neo.getIndex(ecp.getEntityClass(properties[properties.length - 2])).query(new Condition(properties[properties.length - 1], value, compType).toString());
		
		List<Node> results = new ArrayList<Node>();
		for(Node n : nodes)
			results.add(n);
		
		List<Node> related;
		
		for(int i = properties.length - 3; i >= 0; i--){
			related = new ArrayList<Node>();
			for(Node n : results){
				RelationshipType relationType = neo.getMappingInfo(ecp.getEntityClass(properties[i])).getRelationshipType(properties[i+1]);
				for(Relationship relation : n.getRelationships(Direction.INCOMING, relationType))
					related.add(relation.getStartNode());
			}results = related;
		}
		
		related = new ArrayList<Node>();
		for(Node n : results){
			RelationshipType relationType = neo.getMappingInfo(clazz).getRelationshipType(properties[0]);
			for(Relationship relation : n.getRelationships(Direction.INCOMING, relationType))
				related.add(relation.getStartNode());
		}results = related;
		
		List<Condition> conditions = new ArrayList<Condition>();
		for(Node n : results){
			conditions.add(new Condition(neo.getMappingInfo(clazz).getId(), n.getProperty(neo.getMappingInfo(clazz).getId())));
		}
		return new Condition(ConditionType.OR, conditions);
	}
	
	private Query<?> getQuery(Class<?> clazz) {
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		Neo4J ds = dsp.getDatastore();
		return ds.query(clazz);
	}
	
	@Override
	public String toString(){
		String s = propertyName;
		s += " (" + paramName + ") ";
		s += compType + " ";
		return s;
	}
}
