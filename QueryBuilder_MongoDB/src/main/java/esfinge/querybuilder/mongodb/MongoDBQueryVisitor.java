package esfinge.querybuilder.mongodb;

import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import esfinge.querybuilder.core.methodparser.ComparisonType;
import esfinge.querybuilder.core.methodparser.OrderingDirection;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryOrder;
import esfinge.querybuilder.core.methodparser.QueryRepresentation;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import esfinge.querybuilder.core.methodparser.QueryVisitor;
import esfinge.querybuilder.core.methodparser.conditions.NullOption;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import esfinge.querybuilder.core.utils.ServiceLocator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MongoDBQueryVisitor implements QueryVisitor {

    private Query<?> query;
    private final QueryInfo info;
    private final Object[] args;
    private final List<ConditionDescription> conditions = new ArrayList<>();
    private final List<QueryOrder> order = new ArrayList<>();

    public MongoDBQueryVisitor(QueryInfo info, Object args[]) {
        this.info = info;
        this.args = args;
        info.visit(this);
    }

    @Override
    public void visitEntity(String entity) {
        var ds = ServiceLocator.getServiceImplementation(DatastoreProvider.class).getDatastore();
        query = ds.find(info.getEntityType());
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
    }

    @Override
    public Object getFixParameterValue(String param) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getFixParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
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
        addConditions();
    }

    protected void addConditions() {
        var filtersOR = new ArrayList<List<Filter>>();
        filtersOR.add(new ArrayList<>());

        var fixedValueIndex = 0;
        var formatters = info.getCondition().getParameterFormatters();

        for (var i = 0; i < conditions.size(); i++) {
            var cond = conditions.get(i);

            if (cond.getFixedValue() != null) {
                cond.addCondition(filtersOR, info.getEntityType(), formatters.get(fixedValueIndex));
            } else {
                var namedParameters = info.getNamedParemeters();
                var parameterIndex = -1;

                for (var j = 0; j < namedParameters.size(); j++) {
                    if (namedParameters.get(j).equalsIgnoreCase(cond.getPropertyName().replace(".", "") + cond.getCompType().getOpName())) {
                        parameterIndex = j;
                    }
                }

                var formatterIndex = parameterIndex + formatters.size() - namedParameters.size();

                if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {

                    if (args[parameterIndex] != null) {
                        cond.addCondition(filtersOR, formatters.get(formatterIndex).formatParameter(args[parameterIndex]), info.getEntityType());
                    } else {
                        cond.addCondition(filtersOR, null, info.getEntityType());
                    }

                } else if (info.getQueryStyle() == QueryStyle.QUERY_OBJECT) {

                    var paramMap = ReflectionUtils.toParameterMap(args[0]);

                    if (paramMap.get(namedParameters.get(parameterIndex)) != null) {
                        cond.addCondition(filtersOR, formatters.get(formatterIndex).formatParameter(paramMap.get(namedParameters.get(parameterIndex))), info.getEntityType());
                    } else {
                        cond.addCondition(filtersOR, null, info.getEntityType());
                    }

                }
            }
        }

        addConectors(filtersOR);
    }

    protected void addConectors(List<List<Filter>> filtersOR) {
        var orFilters = new ArrayList<Filter>();
        for (var andFilters : filtersOR) {
            if (!andFilters.isEmpty()) {
                if (andFilters.size() > 1) {
                    orFilters.add(Filters.and(andFilters.toArray(Filter[]::new)));
                } else {
                    orFilters.add(andFilters.get(0));
                }
            }
        }

        if (!orFilters.isEmpty()) {
            if (orFilters.size() == 1) {
                query.filter(orFilters.get(0));
            } else {
                query.filter(Filters.or(orFilters.toArray(Filter[]::new)));
            }
        }
    }

    private Map<String, Object> getFixParameterMap() {
        Map<String, Object> fixParameters = new HashMap<>();
        for (var cond : conditions) {
            if (cond.getFixedValue() != null) {
                fixParameters.put(cond.getParamName(), cond.getFixedValue());
            }
        }
        return fixParameters;
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return new MongoDBQueryRepresentation(query, isDynamic(), getFixParameterMap());
    }

    public void printConditions() {
        for (var cond : conditions) {
            System.out.println(cond);
        }
    }
}
