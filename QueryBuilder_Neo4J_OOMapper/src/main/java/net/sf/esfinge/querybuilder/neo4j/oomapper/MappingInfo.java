package net.sf.esfinge.querybuilder.neo4j.oomapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.neo4j.graphdb.RelationshipType;

public class MappingInfo {

	private ArrayList<String> properties = new ArrayList<String>();
	private ArrayList<String> indexedProperties = new ArrayList<String>();
	private String id = null;
	
	private HashMap<String, Class<?>> relatedTo = new HashMap<String, Class<?>>();
	private Class<?> clazz;
	
	public MappingInfo(Class<?> clazz){
		this.clazz = clazz;
	}
	
	public void addProperty(String property){
		properties.add(property);
	}
	
	public void addIndexedProperty(String indexedProperty){
		indexedProperties.add(indexedProperty);
	}
	
	public void addRelationship(String fieldName, Class<?> related){
		relatedTo.put(fieldName, related);
	}
	
	public ArrayList<String> getProperties(){
		return properties;
	}
	
	public ArrayList<String> getIndexedProperties(){
		return indexedProperties;
	}
	
	public boolean isRelatedProperty(String propertyName){
		if(getRelatedEntities().contains(propertyName.substring(0, propertyName.indexOf("."))))
			return true;
		return false;		
	}
	
	public boolean isMappedProperty(String propertyName){
		if(properties.contains(propertyName))
			return true;
		if(indexedProperties.contains(propertyName))
			return true;
		if(id.equals(propertyName))
			return true;
		return false;
	}

	public boolean isIndexedProperty(String propertyName) {
		if(indexedProperties.contains(propertyName))
			return true;
		if(id.equals(propertyName))
			return true;
		return false;
	}

	public Class<?> getClazz(){
		return clazz;
	}
	
	public Set<String> getRelatedEntities(){
		return relatedTo.keySet();
	}
	
	public Class<?> getRelatedClass(String propertyName){
		return relatedTo.get(propertyName);
	}

	public RelationshipType getRelationshipType(String relatedEntityName){
		return RelationshipType.withName(relatedEntityName);
	}

	public void setId(String name) {
		id = name;
	}

	public boolean isIdSet() {
		if(id == null)
			return false;
		return true;
	}
	
	public String getId(){
		return id;
	}
}
