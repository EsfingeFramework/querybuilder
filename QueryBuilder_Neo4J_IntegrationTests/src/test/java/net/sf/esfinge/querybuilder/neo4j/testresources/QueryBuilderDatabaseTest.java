package net.sf.esfinge.querybuilder.neo4j.testresources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Neo4jSession;

public class QueryBuilderDatabaseTest {
	
	protected void initializeDatabase(String filename) throws DataSetException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, NoSuchFieldException, InvocationTargetException {
				
		DataFileLoader loader = new FlatXmlDataFileLoader();
		IDataSet dataSet = loader.load(filename);

		TestNeo4JDatastoreProvider dsp = new TestNeo4JDatastoreProvider();
		dsp.clear();
		Neo4jSession ds = dsp.getDatastore();
		
		for(String tableName : dataSet.getTableNames()){
			ITable table = dataSet.getTable(tableName);
			for(int row = 0; row < table.getRowCount(); row++){
				Class<?> tableEntityClass = dsp.getEntityClass(tableName);
				if(tableEntityClass == null){
					System.err.println(tableName + " not found!");
					continue;
				}
				Object entity = tableEntityClass.newInstance();
				for(Column column : table.getTableMetaData().getColumns()){
					
					String columnName = column.getColumnName();
					String value = (String) table.getValue(row, columnName);
					boolean encontrou = false;
					for(Field field : entity.getClass().getDeclaredFields()){
						if(field.getName().equalsIgnoreCase(columnName)){
							setField(entity, value, field);
							encontrou = true;
							break;
						}
					}
					
					if(!encontrou){
						String foreignCollection = columnName.substring(0, columnName.indexOf("_"));
						String foreignKey = columnName.substring(columnName.indexOf("_") + 1);
						Class<?> foreignObjectClass = dsp.getEntityClass(foreignCollection);
						Field foreignField = null;
						for(Field field : foreignObjectClass.getDeclaredFields()){
							if(field.getName().equalsIgnoreCase(foreignKey)){
								foreignField = field;
								break;
							}
						}
						
						Filter filter = new Filter(foreignField.getName(), ComparisonOperator.EQUALS, Long.parseLong(value));
						Collection<?> entities = ds.loadAll(foreignObjectClass, filter);
						
						if(!entities.isEmpty()) {
							setField(entity, entities.iterator().next());
						}
						
					}
					
				}
				ds.save(entity);
			}
		}
	}
	
	private Object getCorrectFieldValue(Field field, String value){
		if(field.getType() == String.class)
			return value;
		else if(field.getType() == Short.class || field.getType() == short.class)
			return Short.parseShort(value);
		else if(field.getType() == Integer.class || field.getType() == int.class)
			return Integer.parseInt(value);
		else if(field.getType() == Long.class || field.getType() == long.class)
			return Long.parseLong(value);
		else if(field.getType() == Float.class || field.getType() == float.class)
			return Float.parseFloat(value);
		else if(field.getType() == Double.class || field.getType() == double.class)
			return Double.parseDouble(value);
		else if(field.getType() == Character.class || field.getType() == char.class)
			return value.charAt(0);
		else if(field.getType() == Byte.class || field.getType() == byte.class)
			return Byte.decode(value);
		else if(field.getType() == Boolean.class || field.getType() == boolean.class)
			return Boolean.parseBoolean(value);
		else{
			System.err.println("The field type " + field.getType() + " is not supported by the initializeDatabase method!");
			return null;
		}
	}
	
	private void setField(Object o, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		for(Method method : o.getClass().getMethods()){
			if(method.getName().substring(0, 3).equalsIgnoreCase("set") && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == value.getClass()){
				method.invoke(o, value);
			}
		}
	}

	private void setField(Object o, String value, Field field) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for(Method method : o.getClass().getMethods()){
			if(method.getName().equalsIgnoreCase("set" + field.getName())){
				method.invoke(o, getCorrectFieldValue(field, value));
			}
		}
	}

	protected void compareTables(String expectedDatasetFileName, String... tableNames) throws Exception {
		/*IDataSet databaseDataSet = jdt.getConnection().createDataSet();
		IDataSet expectedDataSet = new FlatXmlDataFileLoader().load(expectedDatasetFileName);
		for (String tableName : tableNames) {
			ITable actualTable = databaseDataSet.getTable(tableName);
			ITable expectedTable = expectedDataSet.getTable(tableName);
			Assertion.assertEquals(expectedTable, actualTable);
		}*/
	}

}