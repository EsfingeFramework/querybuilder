package org.esfinge.querybuilder.mongodb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;

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
	
	public void addCondition(ArrayList<ArrayList<Criteria>> criteria, Class<?> clazz, ParameterFormater formatter) {
		addCondition(criteria, formatter.formatParameter(fixedValue), clazz);
	}
	
	public void addCondition(ArrayList<ArrayList<Criteria>> criteria, Object value, Class<?> clazz) {
		
		if(!(value == null && nullOption == NullOption.IGNORE_WHEN_NULL)){
		
			Criteria newCriteria;
			boolean composite = false;
			
			if(propertyName.indexOf(".") != -1){
				for(Field field : clazz.getDeclaredFields()){
					if(field.getName().equalsIgnoreCase(propertyName.substring(0, propertyName.indexOf(".")))){
						if(field.isAnnotationPresent(Reference.class))
							composite = true;
					}
				}
			}
			
			if(value != null){
				
				if(composite){
					newCriteria = compositeProperty(clazz, value);
				}
				else{
				
					switch(compType){
						case GREATER_OR_EQUALS: newCriteria = getQuery(clazz).criteria(propertyName).greaterThanOrEq(value); break;
						case LESSER_OR_EQUALS: newCriteria = getQuery(clazz).criteria(propertyName).lessThanOrEq(value); break;
						case GREATER: newCriteria = getQuery(clazz).criteria(propertyName).greaterThan(value); break;
						case LESSER: newCriteria = getQuery(clazz).criteria(propertyName).lessThan(value); break;
						case NOT_EQUALS: newCriteria = getQuery(clazz).criteria(propertyName).notEqual(value); break;
						case EQUALS: newCriteria = getQuery(clazz).criteria(propertyName).equal(value); break;
						default: newCriteria = getQuery(clazz).criteria(propertyName).contains((String)value); break;
					}
				}
					
			}
			else{
				
				if(composite)
					newCriteria = getQuery(clazz).criteria(propertyName.substring(0, propertyName.indexOf("."))).equal(null);
				else
					newCriteria = getQuery(clazz).criteria(propertyName).equal(null);
			
			}
			criteria.get(criteria.size() - 1).add(newCriteria);
			
			if(nextConector.equals("and")){
				//Do nothing
			}else if(nextConector.equals("or")){
				criteria.add(new ArrayList<Criteria>());
			}else if(nextConector.equals("")){
				//Do nothing
			}else{
				System.err.println("Unsupported connector!");
			}
		}
	}
	
	private Criteria compositeProperty(Class<?> clazz, Object value){
		
		EntityClassProvider ecp = ServiceLocator.getServiceImplementation(EntityClassProvider.class);
		
		String[] properties = propertyName.split("\\.");
		
		Query<?> q = getQuery(ecp.getEntityClass(properties[properties.length - 2]));
		
		switch(compType){
			case GREATER_OR_EQUALS: q.field(properties[properties.length - 1]).greaterThanOrEq(value); break;
			case LESSER_OR_EQUALS: q.field(properties[properties.length - 1]).lessThanOrEq(value); break;
			case GREATER: q.field(properties[properties.length - 1]).greaterThan(value); break;
			case LESSER: q.field(properties[properties.length - 1]).lessThan(value); break;
			case NOT_EQUALS: q.field(properties[properties.length - 1]).notEqual(value); break;
			case EQUALS: q.field(properties[properties.length - 1]).equal(value); break;
			default: q.field(properties[properties.length - 1]).contains((String)value); break;
		}
		
		List<?> results = q.asList();
		
		for(int i = properties.length - 3; i >= 0; i--){
			Query<?> query = getQuery(ecp.getEntityClass(properties[i]));
			query.field(properties[i + 1]).in(results);
			results = query.asList();
		}
		
		return getQuery(clazz).criteria(properties[0]).in(results);
	}
	
	private Query<?> getQuery(Class<?> clazz) {
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		Datastore ds = dsp.getDatastore();
		return ds.createQuery(clazz);
	}
	
	@Override
	public String toString(){
		String s = propertyName;
		s += " (" + paramName + ") ";
		s += compType + " ";
		return s;
	}
}
