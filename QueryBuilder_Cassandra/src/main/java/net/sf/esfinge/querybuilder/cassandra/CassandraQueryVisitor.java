package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedComparisonTypeException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ConditionStatement;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.*;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.*;

public class CassandraQueryVisitor implements QueryVisitor {


    private final List<ConditionStatement> conditions = new ArrayList<>();
    private String entity;
    private QueryElement lastCalled = QueryElement.NONE;
    private String query = "";

    @Override
    public void visitEntity(String s) {
        lastCalled = QueryElement.ENTITY;

        this.entity = s;
    }

    @Override
    public void visitConector(String s) {
        if (!s.equalsIgnoreCase("AND") && !s.equalsIgnoreCase("OR"))
            throw new InvalidConnectorException("Invalid connector \"" + s + "\", valid values are: AND, OR");

        conditions.get(conditions.size() - 1).setNextConnector(s.toUpperCase());

        lastCalled = QueryElement.CONECTOR;
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType) {
        // Cassandra supports only these conditional operators in the WHERE clause:
        // CONTAINS, CONTAINS KEY, IN, =, >, >=, <, or <=, but not all in certain situations.
        if (comparisonType == ComparisonType.NOT_EQUALS || comparisonType == ComparisonType.STARTS || comparisonType == ComparisonType.ENDS)
            throw new UnsupportedComparisonTypeException(comparisonType + " not supported in Cassandra");

        conditions.add(new ConditionStatement(parameter, comparisonType));

        lastCalled = QueryElement.CONDITION;
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, NullOption nullOption) {

    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, Object o) {
        visitCondition(parameter,comparisonType);

        conditions.get(conditions.size()-1).setValue(o);

        lastCalled = QueryElement.CONDITION;
    }

    @Override
    public void visitOrderBy(String s, OrderingDirection orderingDirection) {

    }

    @Override
    public void visitEnd() {
        if (lastCalled == QueryElement.NONE)
            throw new InvalidQuerySequenceException(
                    "Cannot end an empty query sequence.");

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM " + entity);

        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(getConditions());
        }

        sb.append(addOrderBy());

        query = sb.toString();
    }

    private String addOrderBy() {
        StringBuilder sb = new StringBuilder();
        // TODO: ORDER BY IS NOT PROPERLY SUPPORTED BY CASSANDRA, BETTER TO IMPLEMENT ON APPLICATION LOGIC

        return sb.toString();
    }

    private String getConditions() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < conditions.size(); i++) {
            sb.append(conditions.get(i).toString());

            if (i < conditions.size() - 1)
                sb.append(" " + conditions.get(i).getNextConnector() + " ");
        }

        return sb.toString();
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    @Deprecated
    public String getQuery(Map<String, Object> map) {
        throw new UnsupportedOperationException("getQuery method in CassandraQueryVisitor not supported, use CassandraQueryRepresentation instead.");
    }

    @Override
    @Deprecated
    public Set<String> getFixParameters() {
        throw new UnsupportedOperationException("getFixParameters method in CassandraQueryVisitor not supported, use CassandraQueryRepresentation instead.");
    }

    @Override
    @Deprecated
    public Object getFixParameterValue(String s) {
        throw new UnsupportedOperationException("getFixParameterValue method in CassandraQueryVisitor not supported, use CassandraQueryRepresentation instead...");
    }

    private Map<String, Object> getFixParametersMap(){
        Map<String, Object> fixParameters = new HashMap<String, Object>();
        for(ConditionStatement cond : conditions){
            if(cond.getValue() != null){
                fixParameters.put(cond.getPropertyName(), cond.getValue());
            }
        }
        return fixParameters;
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        QueryRepresentation qr = new CassandraQueryRepresentation(getQuery(), isDynamic(), getFixParametersMap());
        return qr;
    }
}
