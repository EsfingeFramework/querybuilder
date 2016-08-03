package org.esfinge.querybuilder.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.methodparser.OrderingDirection;
import org.esfinge.querybuilder.methodparser.QueryInfo;
import org.esfinge.querybuilder.methodparser.QueryOrder;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.QueryStyle;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;
import org.esfinge.querybuilder.neo4j.oomapper.Condition;
import org.esfinge.querybuilder.neo4j.oomapper.Neo4J;
import org.esfinge.querybuilder.neo4j.oomapper.Query;
import org.esfinge.querybuilder.utils.ReflectionUtils;
import org.esfinge.querybuilder.utils.ServiceLocator;

@SuppressWarnings("rawtypes")
public class Neo4JQueryVisitor implements QueryVisitor{
	
	private Query query;
	private QueryInfo info;
	private Object[] args;
	private List<ConditionDescription> conditions = new ArrayList<ConditionDescription>();
	private List<QueryOrder> order = new ArrayList<QueryOrder>();

	public Neo4JQueryVisitor(QueryInfo info, Object args[]){
		this.info = info;
		this.args = args;
		info.visit(this);
	}
	
	@Override
	public void visitEntity(String entity) {
		Neo4J neo = ServiceLocator.getServiceImplementation(DatastoreProvider.class).getDatastore();
		query = neo.query(info.getEntityType());
	}

	@Override
	public void visitConector(String conector) {
		conditions.get(conditions.size()-1).setNextConector(conector);
	}

	@Override
	public void visitCondition(String propertyName, ComparisonType op) {
		ConditionDescription cond = new ConditionDescription(propertyName, op);
		conditions.add(cond);
	}

	@Override
	public void visitCondition(String propertyName, ComparisonType operator, Object fixedValue) {
		ConditionDescription cond = new ConditionDescription(propertyName, operator);
		cond.setFixedValue(fixedValue);
		conditions.add(cond);
	}
	
	@Override
	public void visitCondition(String propertyName, ComparisonType op, NullOption nullOption) {
		ConditionDescription cond = new ConditionDescription(propertyName, op);
		cond.setNullOption(nullOption);
		conditions.add(cond);
	}
	
	protected String getParamName(String propertyName) {
		return propertyName.substring(propertyName.lastIndexOf(".")+1);
	}

	public String getQuery() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void visitOrderBy(String property, OrderingDirection dir) {
		QueryOrder qo = new QueryOrder(property, dir);
		order.add(qo);
	}

	@Override
	public Object getFixParameterValue(String param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getFixParameters() {
		throw new UnsupportedOperationException();
	}

	public boolean isDynamic() {
		for(ConditionDescription cond : conditions){
			if(cond.getNullOption() != NullOption.NONE){
				return true;
			}
		}
		return false;
	}

	public String getQuery(Map<String, Object> params) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visitEnd() {
		addConditions();
		addOrderBy();
	}

	protected void addConditions() {
		
		ArrayList<ArrayList<Condition>> criteriaOR = new ArrayList<ArrayList<Condition>>();
		criteriaOR.add(new ArrayList<Condition>());
		
		int fixedValueIndex = 0;
		List<ParameterFormater> formatters = info.getCondition().getParameterFormatters();
		
		for(int i=0; i < conditions.size(); i++){
			ConditionDescription cond = conditions.get(i);
			
			if(cond.getFixedValue() != null){
				cond.addCondition(criteriaOR, info.getEntityType(), formatters.get(fixedValueIndex));
			}else{
				
				List<String> namedParameters = info.getNamedParemeters();
				
				int parameterIndex = -1;
				
				for(int j = 0; j < namedParameters.size(); j++){
					if(namedParameters.get(j).equalsIgnoreCase(cond.getPropertyName().replace(".", "") + cond.getCompType().getOpName()))
						parameterIndex = j;
				}
				
				int formatterIndex = parameterIndex + formatters.size() - namedParameters.size();
				
				
				if(info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE){
					
					if(args[parameterIndex] != null)
						cond.addCondition(criteriaOR, formatters.get(formatterIndex).formatParameter(args[parameterIndex]), info.getEntityType());
					else
						cond.addCondition(criteriaOR, null, info.getEntityType());
					
				}else if(info.getQueryStyle() == QueryStyle.QUERY_OBJECT){
					
					Map<String,Object> paramMap = ReflectionUtils.toParameterMap(args[0]);
					
					if(paramMap.get(namedParameters.get(parameterIndex)) != null){
						cond.addCondition(criteriaOR, formatters.get(formatterIndex).formatParameter(paramMap.get(namedParameters.get(parameterIndex))), info.getEntityType());
					}else
						cond.addCondition(criteriaOR, null, info.getEntityType());					
					
				}
			}
		}
		
		addConectors(criteriaOR);
		
	}

	protected void addOrderBy() {
		
		String[] orders = new String[order.size()];
		
		for(int i=0; i < order.size(); i++){
			
			orders[i] = "";
			
			if(order.get(i).getDiretion() == OrderingDirection.DESC){
				orders[i] += "-";
			}
			
			orders[i] += order.get(i).getProperty();
		}
		
		if(order.size() != 0)
			query.orderBy(orders);
	}

	protected void addConectors(ArrayList<ArrayList<Condition>> criteriaOR) {
		
		ArrayList<Condition> OR = new ArrayList<Condition>();
		for(ArrayList<Condition> criteriaAND : criteriaOR){
			if(!criteriaAND.isEmpty()){
				if(criteriaAND.size() != 1)
					OR.add(createQuery().and(criteriaAND.toArray(new Condition[0])));
				else
					OR.add(criteriaAND.get(0));
			}
		}
		
		if(!OR.isEmpty()){
			if(OR.size() == 1){
				query.or(OR.get(0));
			}else
				query.or(OR.toArray(new Condition[0]));
		}
	}
	
	private Map<String, Object> getFixParameterMap(){
		Map<String, Object> fixParameters = new HashMap<String, Object>();
		for(ConditionDescription cond : conditions){
			if(cond.getFixedValue() != null){
				fixParameters.put(cond.getParamName(), cond.getFixedValue());
			}
		}
		return fixParameters;
	}

	@Override
	public QueryRepresentation getQueryRepresentation() {
		Neo4JQueryRepresentation qr = new Neo4JQueryRepresentation(query, isDynamic(), getFixParameterMap());
		return qr;
	}

	private Query<?> createQuery() {
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		Neo4J neo = dsp.getDatastore();
		return neo.query(Object.class);
	}
	
	public void printConditions(){
		for(ConditionDescription cond : conditions)
			System.out.println(cond);
	}
}
