package ef.qb.cassandra.validation;

import ef.qb.cassandra.CassandraQueryVisitor;
import static ef.qb.cassandra.config.ConfigReader.getConfiguration;
import ef.qb.cassandra.exceptions.SecondaryQueryLimitExceededException;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.OrderingDirection;
import ef.qb.core.methodparser.QueryRepresentation;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.conditions.NullOption;
import java.util.Map;
import java.util.Set;

public class CassandraChainQueryVisitor implements QueryVisitor {

    private final CassandraQueryVisitor primaryVisitor;
    private final int queryDepth;
    private CassandraChainQueryVisitor secondaryVisitor;

    public CassandraChainQueryVisitor(int queryDepth) {
        this.primaryVisitor = new CassandraQueryVisitor();
        this.queryDepth = queryDepth;
    }

    public CassandraChainQueryVisitor(int queryDepth, CassandraQueryVisitor previousVisitor) {
        this.primaryVisitor = new CassandraQueryVisitor(previousVisitor);
        this.queryDepth = queryDepth;
    }

    @Override
    public void visitEntity(String entity) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitEntity(entity);
        }
    }

    @Override
    public void visitConector(String connector) {
        if (secondaryVisitor == null) {
            if (connector.equalsIgnoreCase("OR")) {
                primaryVisitor.visitEnd();

                int queryLimit = getConfiguration().getSecondaryQueryLimit();

                if (queryDepth >= queryLimit) {
                    throw new SecondaryQueryLimitExceededException("Current query depth is " + queryDepth + ", but the configured limit is " + queryLimit);
                }

                secondaryVisitor = new CassandraChainQueryVisitor(this.queryDepth + 1, primaryVisitor);
                secondaryVisitor.visitEntity(primaryVisitor.getEntity());
            } else {
                primaryVisitor.visitConector(connector);
            }

        } else {
            secondaryVisitor.visitConector(connector);
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitCondition(parameter, comparisonType);
        } else {
            secondaryVisitor.visitCondition(parameter, comparisonType);
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, NullOption nullOption) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitCondition(parameter, comparisonType, nullOption);
        } else {
            secondaryVisitor.visitCondition(parameter, comparisonType, nullOption);
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, Object value) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitCondition(parameter, comparisonType, value);
        } else {
            secondaryVisitor.visitCondition(parameter, comparisonType, value);
        }
    }

    @Override
    public void visitOrderBy(String parameter, OrderingDirection orderingDirection) {
        if (secondaryVisitor == null) {
            primaryVisitor.visitOrderBy(parameter, orderingDirection);
        } else {
            secondaryVisitor.visitOrderBy(parameter, orderingDirection);
        }
    }

    @Override
    public void visitEnd() {
        if (secondaryVisitor == null) {
            primaryVisitor.visitEnd();
        } else {
            secondaryVisitor.visitEnd();
        }
    }

    @Override
    public boolean isDynamic() {
        return primaryVisitor.isDynamic();
    }

    @Override
    public String getQuery() {
        return primaryVisitor.getQuery();
    }

    @Override
    public String getQuery(Map<String, Object> map) {
        return primaryVisitor.getQuery(map);
    }

    @Override
    public Set<String> getFixParameters() {
        return primaryVisitor.getFixParameters();
    }

    @Override
    public Object getFixParameterValue(String s) {
        return primaryVisitor.getFixParameterValue(s);
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return primaryVisitor.getQueryRepresentation();
    }

    public CassandraChainQueryVisitor getSecondaryVisitor() {
        return secondaryVisitor;
    }

}
