package org.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.esfinge.querybuilder.exception.EntityClassNotFoundException;
import org.esfinge.querybuilder.methodparser.conditions.CompositeCondition;
import org.esfinge.querybuilder.methodparser.conditions.NullCondition;
import org.esfinge.querybuilder.methodparser.conditions.QueryCondition;

public class QueryInfo {
	
	private String entityName;
	private Class entityType;
	private QueryType queryType;
	private QueryCondition condition = new NullCondition();
	private List<QueryOrder> order = new ArrayList<QueryOrder>();
	private QueryStyle queryStyle;
	
	public QueryStyle getQueryStyle() {
		return queryStyle;
	}

	public void setQueryStyle(QueryStyle queryStyle) {
		this.queryStyle = queryStyle;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public String getEntityName() {
		return entityName;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public QueryCondition getCondition() {
		return condition;
	}
	
	public void addCondition(QueryCondition qp){
		addCondition(qp, "and");
	}
	
	public Class getEntityType() {
		return entityType;
	}

	public void setEntityType(Class entityType) {
		if(entityType == null){
			throw new EntityClassNotFoundException("The entity class was not found by the class provider");
		}
		this.entityType = entityType;
	}

	public void addCondition(QueryCondition qp, String conector){
		if(qp instanceof NullCondition){
			return;
		}else if(condition instanceof NullCondition){
			condition = qp;	
		}else{
			condition = new CompositeCondition(condition, conector, qp);
		}
	}
	
	public void addOrdering(String property, OrderingDirection direction){
		order.add(new QueryOrder(property, direction));
	}

	public void visit(QueryVisitor visitor) {
		visitor.visitEntity(entityName);
		condition.visit(visitor);
		for(QueryOrder o : order){
			o.visit(visitor);
		}
		visitor.visitEnd();
	}

	public List<String> getNamedParemeters() {
		return condition.getMethodParameterNames();
	}

	public void setQueryType(Method m) {
		if(Collection.class.isAssignableFrom(m.getReturnType())){
			setQueryType(QueryType.RETRIEVE_LIST);
		}else{
			setQueryType(QueryType.RETRIEVE_SINGLE);
		}
	}

	public boolean isDynamic() {
		return condition.isDynamic();
	}
	
	
	
	
	
	
	
	
	
	
	private int pageNumberParameterIndex = -1;
	private int pageSizeParameterIndex = -1;
	private Integer pageSize;
	
	public int getPageNumberParameterIndex() {
		return pageNumberParameterIndex;
	}

	public void setPageNumberParameterIndex(int index) {
		pageNumberParameterIndex = index;
	}
	
	public int getPageSizeParameterIndex() {
		return pageSizeParameterIndex;
	}

	public void setPageSizeParameterIndex(int index) {
		pageSizeParameterIndex = index;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(int size) {
		pageSize = size;
	}

}
