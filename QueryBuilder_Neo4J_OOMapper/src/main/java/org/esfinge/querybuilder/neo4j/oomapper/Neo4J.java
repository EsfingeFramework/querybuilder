package org.esfinge.querybuilder.neo4j.oomapper;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.esfinge.querybuilder.neo4j.oomapper.parser.Parser;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.ClassNotMappedException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.InvalidRemovalException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.NullIdException;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.test.TestGraphDatabaseFactory;


public class Neo4J {
	
	private GraphDatabaseService graphdb;
	private HashMap<Class<?>, MappingInfo> classInfoMap = new HashMap<Class<?>, MappingInfo>();
	private HashMap<String, Class<?>> nameClassMap = new HashMap<String, Class<?>>();
	private Parser parser = new Parser(this);

	public Neo4J(String databasePath){
		graphdb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(databasePath));
	}
	
	public Neo4J(){
		graphdb = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();
	}
	
	public void map(Class<?> clazz) throws RuntimeException{
		
		MappingInfo info = parser.parse(clazz);
		classInfoMap.put(clazz, info);
		nameClassMap.put(clazz.getSimpleName().toLowerCase(), clazz);
		
	}
	
	public Class<?> getEntityClass(String entityName){
		return nameClassMap.get(entityName.toLowerCase());
	}
	
	public void shutdown(){
		graphdb.shutdown();
	}
	
	public void clearDB(){
		
		Transaction t = beginTx();
		
		try{
			for(Node node : graphdb.getAllNodes()){
				for(Relationship rel : node.getRelationships())
					rel.delete();
				node.delete();
			}
			classInfoMap = new HashMap<Class<?>, MappingInfo>();
			nameClassMap = new HashMap<String, Class<?>>();
			successTx(t);
		}
		catch(Exception e){
			failureTx(t);
			e.printStackTrace();
		}
	}


	@SuppressWarnings("rawtypes")
	private Node persist(Object entity) {
		
		if(!classInfoMap.containsKey(entity.getClass()))
			throw new ClassNotMappedException(entity.getClass());

		Transaction t = beginTx();
		Node newNode = null;

		try{
			MappingInfo info = classInfoMap.get(entity.getClass());
			newNode = graphdb.createNode();
			
			Label label = Label.label(entity.getClass().getSimpleName());
			newNode.addLabel(label);
			
			String id = info.getId();
			Object idValue = parser.getPropertyValue(id, entity);
			if(idValue == null)
				throw new NullIdException("The entity " + entity + " cannot be saved with null Id");
			newNode.setProperty(id, idValue);
			
			Node present = graphdb.index().forNodes(entity.getClass().getName()).putIfAbsent(newNode, id, idValue);
			
			if(present != null)
				newNode = present;
			
			for(String property : info.getProperties()){
				Object value = parser.getPropertyValue(property, entity);
				if(value != null)
					newNode.setProperty(property, value);
				else
					newNode.setProperty(property, "null");
			}

			for(String property : info.getIndexedProperties()){
				Object value = parser.getPropertyValue(property.substring(property.lastIndexOf(".") + 1), entity);
				if(present != null)
					graphdb.index().forNodes(entity.getClass().getName()).remove(present, property);
				if(value != null){
					newNode.setProperty(property, value);
					graphdb.index().forNodes(entity.getClass().getName()).add(newNode, property, value);
				}else{
					newNode.setProperty(property, "null");
					graphdb.index().forNodes(entity.getClass().getName()).add(newNode, property, "null");
				}
			}

			for(String relatedEntity : info.getRelatedEntities()){
				
				Object related = parser.getRelatedEntity(entity, relatedEntity);
				
				if(present != null){
					for(Relationship relation : present.getRelationships(info.getRelationshipType(relatedEntity))){
						Node otherNode = relation.getOtherNode(present);
						relation.delete();
						if(!otherNode.hasRelationship()){
							graphdb.index().forNodes(info.getRelatedClass(relatedEntity).getName()).remove(otherNode);
							otherNode.delete();
						}
					}
				}
				if(Collection.class.isAssignableFrom(related.getClass())){
					Collection c = (Collection) related;
					for(Object o : c){
						Node relatedNode = persist(o);
						newNode.createRelationshipTo(relatedNode, info.getRelationshipType(relatedEntity));
					}
				}
				else{
					Node relatedNode = persist(related);
					newNode.createRelationshipTo(relatedNode, info.getRelationshipType(relatedEntity));
				}
				
			}

			successTx(t);
			return newNode;
			
		}
		catch(Exception e){
			failureTx(t);
			e.printStackTrace();
			return null;
		}
	
	}
	
	public void save(Object entity) {
		persist(entity);
	}
	
	public void delete(Class<?> clazz, Object id){
		MappingInfo info = getMappingInfo(clazz);
		Transaction t = beginTx();
		Index<Node> index = getIndex(clazz);
		
		Node node = index.get(info.getId(), id).getSingle();
		
		
		try{
			deleteNode(node);
			successTx(t);
		}
		catch(Exception e){
			failureTx(t);
			throw new InvalidRemovalException("The entity of " + clazz + " and Id " + id + " cannot be removed because it is part of a Relationship");
		}
		
	}

	public Transaction beginTx() {
		return graphdb.beginTx();
	}
	
	public void successTx(Transaction t) {
		t.success();
		t.close();
	}
	
	public void failureTx(Transaction t) {
		t.failure();
		t.close();
	}

	private void deleteNode(Node node){
		for(Relationship relation : node.getRelationships(Direction.OUTGOING)){
			Node otherNode = relation.getOtherNode(node);
			relation.delete();
			deleteNode(otherNode);
		}
		node.delete();
	}
	
	public <E> Query<E> query(Class<?> clazz){
		return new Query<E>(this, classInfoMap.get(clazz));
	}
	
	public Index<Node> getIndex(Class<?> clazz){
		return graphdb.index().forNodes(clazz.getName());
	}
	
	public MappingInfo getMappingInfo(Class<?> clazz){
		return classInfoMap.get(clazz);
	}
	
	protected Parser getParser(){
		return parser;
	}

}
