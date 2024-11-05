package ef.qb.jpa1;

import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.OrderingDirection;
import ef.qb.core.methodparser.QueryOrder;
import ef.qb.core.methodparser.QueryRepresentation;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.conditions.NullOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JPAQLQueryVisitor implements QueryVisitor {

    private String query;
    private String entity;
    private final List<ConditionDescription> conditions = new ArrayList<>();
    private final List<QueryOrder> order = new ArrayList<>();

    @Override
    public void visitEntity(String entity) {
        this.entity = entity;
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

    protected String getParamName(String propertyName) {
        return propertyName.substring(propertyName.lastIndexOf(".") + 1);
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType operator, Object fixedValue) {
        var cond = new ConditionDescription(propertyName, operator);
        cond.setFixedValue(fixedValue);
        conditions.add(cond);
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
    public void visitCondition(String propertyName, ComparisonType op, NullOption nullOption) {
        var cond = new ConditionDescription(propertyName, op);
        cond.setNullOption(nullOption);
        conditions.add(cond);
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
        var sb = new StringBuilder();
        sb.append("SELECT o FROM ").append(entity).append(" o");
        addWhere(sb);
        addConditions(sb);
        addOrderBy(sb);

        query = sb.toString();
    }

    protected void addConditions(StringBuilder sb) {
        var hasOnlyIgnorableBefore = true;
        List<String> previousIgnorableParams = new ArrayList<>();
        for (var i = 0; i < conditions.size(); i++) {
            var cond = conditions.get(i);
            if (i != 0) {
                addConector(sb, hasOnlyIgnorableBefore, previousIgnorableParams, i, cond);
            }

            sb.append(cond.getConditionString());
            if (hasOnlyIgnorableBefore && cond.getNullOption() != NullOption.IGNORE_WHEN_NULL) {
                hasOnlyIgnorableBefore = false;
            } else if (cond.getNullOption() == NullOption.IGNORE_WHEN_NULL) {
                previousIgnorableParams.add(cond.getParamName());
            }
        }
    }

    protected void addOrderBy(StringBuilder sb) {
        for (var i = 0; i < order.size(); i++) {
            if (i == 0) {
                sb.append(" ORDER BY");
            } else {
                sb.append(",");
            }
            sb.append(" o.").append(order.get(i).getProperty()).append(" ").append(order.get(i).getDiretion().name());
        }
    }

    protected void addConector(StringBuilder sb,
            boolean hasOnlyIgnorableBefore,
            List<String> previousIgnorableParams, int i,
            ConditionDescription cond) {
        if (hasOnlyIgnorableBefore || cond.getNullOption() == NullOption.IGNORE_WHEN_NULL) {
            sb.append("#{((");
            if (hasOnlyIgnorableBefore) {
                for (var j = 0; j < previousIgnorableParams.size(); j++) {
                    if (j != 0) {
                        sb.append(" &&");
                    }
                    sb.append(" ").append(previousIgnorableParams.get(j)).append(" == null");
                }
            }
            if (cond.getNullOption() == NullOption.IGNORE_WHEN_NULL) {
                if (hasOnlyIgnorableBefore) {
                    sb.append(") || (");
                }
                sb.append(cond.getParamName()).append(" == null");
            }
            sb.append(")) ? '' : ' ").append(conditions.get(i - 1).getNextConector()).append("'}");
        } else {
            sb.append(" ").append(conditions.get(i - 1).getNextConector());
        }
    }

    private void addWhere(StringBuilder sb) {
        if (conditions.isEmpty()) {
        } else if (hasOneNoIgnorableProperty()) {
            sb.append(" WHERE");
        } else {
            sb.append("#{(onlyNullValues(map))? '' : ' WHERE'}");
        }
    }

    private boolean hasOneNoIgnorableProperty() {
        for (var cond : conditions) {
            if (cond.getNullOption() != NullOption.IGNORE_WHEN_NULL) {
                return true;
            }
        }
        return false;
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
        var qr = new JPAQueryRepresentation(getQuery(), isDynamic(), getFixParameterMap());
        return qr;
    }

}
