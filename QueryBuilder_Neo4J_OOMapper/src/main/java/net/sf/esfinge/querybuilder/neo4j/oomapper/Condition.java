package net.sf.esfinge.querybuilder.neo4j.oomapper;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.UnindexedPropertyException;


public class Condition {

	private String propertyName = null;
	private Object value = null;
	private ArrayList<Condition> or = null;
	private ArrayList<Condition> and = null;
	private ComparisonType comparison = null;
	
	public Condition(String propertyName, Object value){
		this.propertyName = propertyName;
		this.value = value;
	}
	
	public Condition(String propertyName, Object value, ComparisonType comparison){
		this.propertyName = propertyName;
		this.value = value;
		this.comparison = comparison;
	}
	
	public Condition(ConditionType type, Condition... conditions){
		if(type == ConditionType.AND){
			and = new ArrayList<Condition>();
			for(Condition cond : conditions)
				and.add(cond);
		}
		else{
			or = new ArrayList<Condition>();
			for(Condition cond : conditions)
				or.add(cond);
		}
	}
	
	public Condition(ConditionType type, List<Condition> conditions){
		if(type == ConditionType.AND){
			and = new ArrayList<Condition>();
			for(Condition cond : conditions)
				and.add(cond);
		}
		else{
			or = new ArrayList<Condition>();
			for(Condition cond : conditions)
				or.add(cond);
		}
	}
	
	public void verifyIndexedProperties(MappingInfo info){
		if(propertyName != null){
			if(!(info.isIndexedProperty(propertyName)||info.isRelatedProperty(propertyName)))
				throw new UnindexedPropertyException("Property " + propertyName + " of NodeEntity Class " + info.getClazz().getName());
		}else if(or != null){
			for(Condition c : or)
				c.verifyIndexedProperties(info);
		}
		else if(and != null){
			for(Condition c : and)
				c.verifyIndexedProperties(info);
		}
	}
	
	@Override
	public String toString(){
		if(propertyName != null){
			if(comparison == ComparisonType.GREATER)
				return propertyName + ":{" + value + " TO *}";
			else if(comparison == ComparisonType.GREATER_OR_EQUALS)
				return propertyName + ":[" + value + " TO *]";
			else if(comparison == ComparisonType.LESSER)
				return propertyName + ":{* TO " + value + "}";
			else if(comparison == ComparisonType.LESSER_OR_EQUALS)
				return propertyName + ":[* TO " + value + "]";
			else if(comparison == ComparisonType.NOT_EQUALS)
				return "*:* AND NOT " + propertyName + ":\"" + value + "\"";
			else if(comparison == ComparisonType.EQUALS)
				return propertyName + ":\"" + value + "\"";
			else
				return propertyName + ":" + value;
				
		}else if(or != null){
			String query = "(";
			boolean first = true;
			for(Condition condition : or){
				if(!first)
					query += " OR ";
				first = false;
				query += condition;
			}
			query += ")";
			return query;
		}else{
			String query = "(";
			boolean first = true;
			for(Condition condition : and){
				if(!first)
					query += " AND ";
				first = false;
				query += condition;
			}
			query += ")";
			return query;
		}
	}
}
