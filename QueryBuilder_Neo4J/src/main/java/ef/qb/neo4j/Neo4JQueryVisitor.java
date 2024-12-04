package ef.qb.neo4j;

import ef.qb.core.methodparser.ComparisonType;
import static ef.qb.core.methodparser.ComparisonType.CONTAINS;
import static ef.qb.core.methodparser.ComparisonType.ENDS;
import static ef.qb.core.methodparser.ComparisonType.GREATER;
import static ef.qb.core.methodparser.ComparisonType.GREATER_OR_EQUALS;
import static ef.qb.core.methodparser.ComparisonType.LESSER;
import static ef.qb.core.methodparser.ComparisonType.LESSER_OR_EQUALS;
import static ef.qb.core.methodparser.ComparisonType.NOT_EQUALS;
import static ef.qb.core.methodparser.ComparisonType.STARTS;
import ef.qb.core.methodparser.OrderingDirection;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryOrder;
import ef.qb.core.methodparser.QueryRepresentation;
import ef.qb.core.methodparser.QueryStyle;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.conditions.NullOption;
import ef.qb.core.utils.ReflectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.cypher.query.SortOrder.Direction;

public class Neo4JQueryVisitor implements QueryVisitor {

    private Filters filters;
    private QueryInfo info;
    private Object[] args;
    private List<ConditionDescription> conditions = new ArrayList<>();
    private List<QueryOrder> order = new ArrayList<>();
    private String label;
    private SortOrder sortOrder;

    public Neo4JQueryVisitor(QueryInfo info, Object args[]) {
        this.info = info;
        this.args = args;
        info.visit(this);
    }

    @Override
    public void visitEntity(String entity) {
        this.label = entity;
        this.filters = new Filters();
        this.sortOrder = new SortOrder();
    }

    @Override
    public void visitConector(String conector) {
        conditions.get(conditions.size() - 1).setNextConector(conector);
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType op) {
        var cond = new ConditionDescription(propertyName, op);
        conditions.add(cond);
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType operator, Object fixedValue) {
        var cond = new ConditionDescription(propertyName, operator);
        cond.setFixedValue(fixedValue);
        conditions.add(cond);
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType op, NullOption nullOption) {
        var cond = new ConditionDescription(propertyName, op);
        cond.setNullOption(nullOption);
        conditions.add(cond);
    }

    protected String getParamName(String propertyName) {
        return propertyName.substring(propertyName.lastIndexOf(".") + 1);
    }

    @Override
    public String getQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitOrderBy(String property, OrderingDirection dir) {
        var qo = new QueryOrder(property, dir);
        order.add(qo);

        this.sortOrder.add(convertOrderingDirection(dir), property);
    }

    private Direction convertOrderingDirection(OrderingDirection dir) {
        switch (dir) {
            case DESC:
                return Direction.DESC;
            default:
                return Direction.ASC;
        }
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
        for (var cond : conditions) {
            if (cond.getNullOption() != NullOption.NONE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getQuery(Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitEnd() {
        addConditionsToFilters();
//		addOrderBy();	// orderBy constructed as visited
    }

    protected void addConditionsToFilters() {

        String previousConector = null;
        var fixedValueIndex = 0;
        var formatters = info.getCondition().getParameterFormatters();

        for (var i = 0; i < conditions.size(); i++) {
            var condition = conditions.get(i);
            var propertyName = condition.getPropertyName();
            var compType = condition.getCompType();

            Object value = null;
            if (condition.getFixedValue() != null) {
                var parameterFormater = formatters.get(fixedValueIndex);
                value = parameterFormater.formatParameter(condition.getFixedValue());
            } else {

                var namedParameters = info.getNamedParemeters();
                var parameterIndex = -1;
                for (var j = 0; j < namedParameters.size(); j++) {
                    if (namedParameters.get(j).equalsIgnoreCase(propertyName.replace(".", "") + compType.getOpName())) {
                        parameterIndex = j;
                    }
                }

                var formatterIndex = parameterIndex + formatters.size() - namedParameters.size();
                if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
                    if (args[parameterIndex] != null) {
                        value = formatters.get(formatterIndex).formatParameter(args[parameterIndex]);
                    }
                } else if (info.getQueryStyle() == QueryStyle.QUERY_OBJECT) {
                    var paramMap = ReflectionUtils.toParameterMap(args[0]);
                    if (paramMap.get(namedParameters.get(parameterIndex)) != null) {
                        value = formatters.get(formatterIndex).formatParameter(paramMap.get(namedParameters.get(parameterIndex)));
                    }
                }
            }

            if (value == null && NullOption.IGNORE_WHEN_NULL == condition.getNullOption()) {
                continue;
            }

            var filter = buildFilter(propertyName, compType, value);
            previousConector = extractPreviousConector(previousConector, condition, filter);
            this.filters.add(filter);
        }

    }

    private Filter buildFilter(String propertyName, ComparisonType compType, Object value) {
        Filter filter;

        var propertyNamePointIndex = propertyName.lastIndexOf(".");
        var filterPropertyName = propertyName.substring(propertyNamePointIndex + 1);
        var convertCompType = (value != null) ? convertCompType(compType) : ComparisonOperator.IS_NULL;

        if (value == null) {
            filter = new Filter(filterPropertyName, convertCompType);
        } else {
            filter = new Filter(filterPropertyName, convertCompType, value);
        }

        if (isNestedProperty(propertyNamePointIndex)) {
            var nestedPropertyName = propertyName.substring(0, propertyNamePointIndex);
            filter.setNestedPropertyName(nestedPropertyName);
            filter.setNestedPropertyType(nestedPropertyType(info.getEntityType(), nestedPropertyName));
        }

        filter.setNegated(isNotEquals(compType));

        return filter;
    }

    private boolean isNestedProperty(int propertyNamePointIndex) {
        return propertyNamePointIndex != -1;
    }

    private boolean isNotEquals(ComparisonType compType) {
        return ComparisonType.NOT_EQUALS == compType;
    }

    private Class<?> nestedPropertyType(Class<?> entityType, String nestedPropertyName) {
        for (var field : entityType.getDeclaredFields()) {
            if (field.getName().equals(nestedPropertyName)) {
                return field.getType();
            }
        }
        throw new RuntimeException("Nested property '" + nestedPropertyName + "' not found in Class " + entityType);
    }

    private ComparisonOperator convertCompType(ComparisonType compType) {
        // TODO implement all Neo4J negative ComparisonOperator possibilities
        switch (compType) {
            case CONTAINS:
                return ComparisonOperator.CONTAINING;
            case ENDS:
                return ComparisonOperator.ENDING_WITH;
            case EQUALS:
                return ComparisonOperator.EQUALS;
            case GREATER:
                return ComparisonOperator.GREATER_THAN;
            case GREATER_OR_EQUALS:
                return ComparisonOperator.GREATER_THAN_EQUAL;
            case LESSER:
                return ComparisonOperator.LESS_THAN;
            case LESSER_OR_EQUALS:
                return ComparisonOperator.LESS_THAN_EQUAL;
            case NOT_EQUALS:
                return ComparisonOperator.EQUALS; // must setNegated(true) on filter
            case STARTS:
                return ComparisonOperator.STARTING_WITH;
        }
        return null;
    }

    private String extractPreviousConector(String previousConector, ConditionDescription condition, Filter filter) {
        var hasNextConector = false;
        var nextConector = condition.getNextConector();
        hasNextConector = !"".equals(nextConector);

        previousConector = addPreviousConectorToActualFilter(previousConector, filter);

        if (hasNextConector) {
            previousConector = nextConector;
        }

        return previousConector;
    }

    private String addPreviousConectorToActualFilter(String previousConector, Filter filter) {
        if (previousConector != null) {
            if ("and".equals(previousConector)) {
                filter.setBooleanOperator(BooleanOperator.AND);
            } else if ("or".equals(previousConector)) {
                filter.setBooleanOperator(BooleanOperator.OR);
            }
            previousConector = null;
        }
        return previousConector;
    }

    private Map<String, Object> getFixParameterMap() {
        Map<String, Object> fixParameters = new HashMap<>();
        for (var condition : conditions) {
            if (condition.getFixedValue() != null) {
                fixParameters.put(condition.getParamName(), condition.getFixedValue());
            }
        }
        return fixParameters;
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return new Neo4JQueryRepresentation(new Neo4JQueryParameters(this.label, this.filters, this.sortOrder), isDynamic(), getFixParameterMap());
    }

    public void printConditions() {
        for (var cond : conditions) {
            System.out.println(cond);
        }
    }

}
