package esfinge.querybuilder.mongodb;

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
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryImpl;

@SuppressWarnings("rawtypes")
public class MongoDBQueryVisitor implements QueryVisitor {

    private Query query;
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
        query = ds.createQuery(info.getEntityType());
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

    public boolean isDynamic() {
        for (var cond : conditions) {
            if (cond.getNullOption() != NullOption.NONE) {
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

        var criteriaOR = new ArrayList<ArrayList<Criteria>>();
        criteriaOR.add(new ArrayList<>());

        var fixedValueIndex = 0;
        var formatters = info.getCondition().getParameterFormatters();

        for (var i = 0; i < conditions.size(); i++) {
            var cond = conditions.get(i);

            if (cond.getFixedValue() != null) {
                cond.addCondition(criteriaOR, info.getEntityType(), formatters.get(fixedValueIndex));
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
                        cond.addCondition(criteriaOR, formatters.get(formatterIndex).formatParameter(args[parameterIndex]), info.getEntityType());
                    } else {
                        cond.addCondition(criteriaOR, null, info.getEntityType());
                    }

                } else if (info.getQueryStyle() == QueryStyle.QUERY_OBJECT) {

                    var paramMap = ReflectionUtils.toParameterMap(args[0]);

                    if (paramMap.get(namedParameters.get(parameterIndex)) != null) {
                        cond.addCondition(criteriaOR, formatters.get(formatterIndex).formatParameter(paramMap.get(namedParameters.get(parameterIndex))), info.getEntityType());
                    } else {
                        cond.addCondition(criteriaOR, null, info.getEntityType());
                    }

                }
            }
        }

        addConectors(criteriaOR);
    }

    protected void addOrderBy() {
        var orderBy = "";

        for (var i = 0; i < order.size(); i++) {
            if (i != 0) {
                orderBy += ",";
            }
            if (order.get(i).getDiretion() == OrderingDirection.DESC) {
                orderBy += "-";
            }
            orderBy += order.get(i).getProperty();
        }

        if (order.size() != 0) {
            query.order(orderBy);
        }
    }

    protected void addConectors(ArrayList<ArrayList<Criteria>> criteriaOR) {

        var OR = new ArrayList<Criteria>();
        for (var criteriaAND : criteriaOR) {
            if (!criteriaAND.isEmpty()) {
                if (criteriaAND.size() != 1) {
                    OR.add(createQuery().and(criteriaAND.toArray(new Criteria[0])));
                } else {
                    OR.add(criteriaAND.get(0));
                }
            }
        }

        if (!OR.isEmpty()) {
            if (OR.size() == 1) {
                var qi = (QueryImpl) query;
                qi.add(OR.get(0));
            } else {
                query.or(OR.toArray(new Criteria[0]));
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
        var qr = new MongoDBQueryRepresentation(query, isDynamic(), getFixParameterMap());
        return qr;
    }

    private Query<?> createQuery() {
        var dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
        var ds = dsp.getDatastore();
        return ds.createQuery(Object.class);
    }

    public void printConditions() {
        for (var cond : conditions) {
            System.out.println(cond);
        }
    }
}
