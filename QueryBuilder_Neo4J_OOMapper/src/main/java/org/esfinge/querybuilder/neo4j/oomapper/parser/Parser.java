package org.esfinge.querybuilder.neo4j.oomapper.parser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.esfinge.querybuilder.neo4j.oomapper.MappingInfo;
import org.esfinge.querybuilder.neo4j.oomapper.Neo4J;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.Indexed;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.RelatedTo;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.AbsentIdException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.DuplicateIdException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.NoNodeEntityException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.RelatedEntityNotFoundException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.UnsupportedFieldTypeException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class Parser {
	
	private Neo4J neo;
	
	public Parser(Neo4J neo){
		this.neo = neo;
	}

	public MappingInfo parse(Class<?> clazz) {
		
		if(!clazz.isAnnotationPresent(NodeEntity.class))
			throw new NoNodeEntityException();
		
		MappingInfo info = new MappingInfo(clazz);
		
		for(Field field : clazz.getDeclaredFields()){
			if(Collection.class.isAssignableFrom(field.getType()))
				mapCollectionField(field, info);
			else
				mapSimpleField(field, info);
		}
		
		if(!info.isIdSet())
			throw new AbsentIdException("The " + clazz + " must define an Id");
		
		return info;
	}
	
	private void mapCollectionField(Field field, MappingInfo info){
		
		if(field.isAnnotationPresent(Id.class))
			throw new UnsupportedFieldTypeException("A Collection cannot be used as Id: " + field.getName());
		if(field.isAnnotationPresent(RelatedTo.class)){
			if(!Set.class.isAssignableFrom(field.getType()))
				throw new UnsupportedFieldTypeException("Collections of related entities must be of type Set: " + field.getName());
			RelatedTo annotation = field.getAnnotation(RelatedTo.class);
			info.addRelationship(field.getName(), annotation.targetClass());
		}
		else if(field.isAnnotationPresent(Indexed.class))
			throw new UnsupportedFieldTypeException("Cannot index a Collection: " + field.getName());
		else{
			info.addProperty(field.getName());
		}
		
	}
	
	private void mapSimpleField(Field field, MappingInfo info){
		
		if(field.getType().isAnnotationPresent(NodeEntity.class)){
			if(field.isAnnotationPresent(Indexed.class))
				throw new UnsupportedFieldTypeException("Cannot index a Relationship: " + field.getName());
			if(field.isAnnotationPresent(Id.class))
				throw new UnsupportedFieldTypeException("A Relationship cannot be used as Id: " + field.getName());
			info.addRelationship(field.getName(), field.getType());
		}else if(field.isAnnotationPresent(Id.class)){
			if(field.getClass().isArray())
				throw new UnsupportedFieldTypeException("Cannot use an Array as Id: " + field.getName());
			if(info.isIdSet())
				throw new DuplicateIdException("Only one field can be used as Id: " + field.getDeclaringClass().getName());
			info.setId(field.getName());
		}else if(field.isAnnotationPresent(Indexed.class)){
			if(field.getClass().isArray())
				throw new UnsupportedFieldTypeException("Cannot index an Array");
			info.addIndexedProperty(field.getName());
		}else{
			info.addProperty(field.getName());
		}
	}
	
	public Object getPropertyValue(String propertyName, Object entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException {
		
		for(Method method : entity.getClass().getDeclaredMethods()){
			
			if(method.getName().toLowerCase().equals("get"+ propertyName.toLowerCase())){
				
				if(Collection.class.isAssignableFrom(entity.getClass().getDeclaredField(propertyName).getType()))
					return getCollectionFieldValue(method, entity, propertyName);
				else
					return method.invoke(entity);
			
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private Object[] getCollectionFieldValue(Method getter, Object entity, String fieldName) throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Field field = entity.getClass().getDeclaredField(fieldName);
        ParameterizedType fieldMemberType = (ParameterizedType) field.getGenericType();
        Class<?> fieldMemberClass = (Class<?>) fieldMemberType.getActualTypeArguments()[0];
        
        Collection collection = (Collection) getter.invoke(entity);
		
        Object[] values = (Object[]) Array.newInstance(fieldMemberClass, collection.size());
		
		int count = 0;
		for(Object o : collection){
			values[count++] = o;
		}
		
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getEntity(Node node, MappingInfo info) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		Object entity = info.getClazz().newInstance();
		
		for(Field field : info.getClazz().getDeclaredFields()){
			
			for(Method setter : info.getClazz().getMethods()){
				
				if(setter.getName().toLowerCase().equals("set" + field.getName().toLowerCase())){
					
					Object value;
					
					if(field.isAnnotationPresent(Indexed.class)){
						
						if(Collection.class.isAssignableFrom(field.getType()))
							throw new UnsupportedFieldTypeException("Cannot index a Collection");
						if(field.getType().isArray())
							throw new UnsupportedFieldTypeException("Cannot index an Array");
						if(field.getType().isAnnotationPresent(NodeEntity.class))
							throw new UnsupportedFieldTypeException("Cannot index a Relationship");
						
						value = node.getProperty(field.getName());
					
					} else {
						
						if(Collection.class.isAssignableFrom(field.getType()))
							value = retrieveCollectionField(node, field, info);
						else if(field.getType().isArray()){
							value = retrieveArrayField(node, field, info);
						} else{
							value = retrieveSimpleField(node, field, info);
						}
						
					}
					
					setter.invoke(entity, value);
					
				}
			}
			
		}
		return (E) entity;
	}
	
	private Object retrieveSimpleField(Node node, Field field, MappingInfo info) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

		if(field.getType().isAnnotationPresent(NodeEntity.class)){
			Relationship relation = node.getRelationships(info.getRelationshipType(field.getName())).iterator().next();
			Node relatedNode = relation.getOtherNode(node);
			MappingInfo relatedInfo = neo.getMappingInfo(field.getType());
			return getEntity(relatedNode, relatedInfo);
		}
		else{
			return node.getProperty(field.getName());
		}
		
	}

	private Object retrieveArrayField(Node node, Field field, MappingInfo info) throws InstantiationException, IllegalAccessException {
		
		Object property = node.getProperty(field.getName());
		
		if(field.getType() == property.getClass())
			return property;
		else{
			
			String classIdentifier = node.getProperty(field.getName()).toString().substring(0, 2);
			
        	if(classIdentifier.equals(new int[0].toString().substring(0, 2))){
				int[] values = (int[]) node.getProperty(field.getName());
				Integer[] returnVariable = new Integer[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else if(classIdentifier.equals(new long[0].toString().substring(0, 2))){
				long[] values = (long[]) node.getProperty(field.getName());
				Long[] returnVariable = new Long[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else if(classIdentifier.equals(new float[0].toString().substring(0, 2))){
				float[] values = (float[]) node.getProperty(field.getName());
				Float[] returnVariable = new Float[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else if(classIdentifier.equals(new double[0].toString().substring(0, 2))){
				double[] values = (double[]) node.getProperty(field.getName());
				Double[] returnVariable = new Double[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else if(classIdentifier.equals(new byte[0].toString().substring(0, 2))){
				byte[] values = (byte[]) node.getProperty(field.getName());
				Byte[] returnVariable = new Byte[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else if(classIdentifier.equals(new short[0].toString().substring(0, 2))){
				short[] values = (short[]) node.getProperty(field.getName());
				Short[] returnVariable = new Short[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else if(classIdentifier.equals(new boolean[0].toString().substring(0, 2))){
				boolean[] values = (boolean[]) node.getProperty(field.getName());
				Boolean[] returnVariable = new Boolean[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}else {
				char[] values = (char[]) node.getProperty(field.getName());
				Character[] returnVariable = new Character[values.length];
				for(int i = 0; i < values.length; i++){
					returnVariable[i] = values[i];
				}
				return returnVariable;
			}
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object retrieveCollectionField(Node node, Field field, MappingInfo info) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		ParameterizedType fieldMemberType = (ParameterizedType) field.getGenericType();
        Class<?> memberClass = (Class<?>) fieldMemberType.getActualTypeArguments()[0];
        
        if(memberClass.isAnnotationPresent(NodeEntity.class)){
        	
        	if(!Set.class.isAssignableFrom(field.getType()))
				throw new UnsupportedFieldTypeException("Collections of related entities must be of type Set");
        	
        	MappingInfo relatedInfo = neo.getMappingInfo(memberClass);
        	
        	Collection c = new HashSet();
			for(Relationship relation : node.getRelationships(info.getRelationshipType(field.getName()))){
				Node relatedNode = relation.getOtherNode(node);
				c.add(getEntity(relatedNode, relatedInfo));
			}
			
			return c;
        
        } else{
        	
        	String classIdentifier = node.getProperty(field.getName()).toString().substring(0, 2);
			
        	Collection c = (Collection) field.getType().newInstance();
        
			if(classIdentifier.equals(new int[0].toString().substring(0, 2))){
				int[] values = (int[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else if(classIdentifier.equals(new long[0].toString().substring(0, 2))){
				long[] values = (long[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else if(classIdentifier.equals(new float[0].toString().substring(0, 2))){
				float[] values = (float[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else if(classIdentifier.equals(new double[0].toString().substring(0, 2))){
				double[] values = (double[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else if(classIdentifier.equals(new byte[0].toString().substring(0, 2))){
				byte[] values = (byte[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else if(classIdentifier.equals(new short[0].toString().substring(0, 2))){
				short[] values = (short[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else if(classIdentifier.equals(new boolean[0].toString().substring(0, 2))){
				boolean[] values = (boolean[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}else {
				char[] values = (char[]) node.getProperty(field.getName());
				for(Object o : values){
					c.add(o);
				}
				return c;
			}
			
		}
        
	}

	public Object getRelatedEntity(Object entity, String fieldName) {
		for(Method getter : entity.getClass().getMethods()){
			if(getter.getName().toLowerCase().equals("get" + fieldName.toLowerCase())){
				try{
					return getter.invoke(entity);
				}catch(Exception e){
					e.printStackTrace();
					throw new RelatedEntityNotFoundException();
				}
			}
		}
		throw new RelatedEntityNotFoundException();
	}
}
