package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.methodparser.QueryInfo;
import java.util.List;
import java.util.Objects;

public class CompositeQueryExecutor implements QueryExecutor {

    private final QueryExecutor priExecutor;
    private final QueryExecutor secExecutor;
    private final List<RelationProcessor> relationProcessors;

    public CompositeQueryExecutor(QueryExecutor priExecutor, QueryExecutor secExecutor) {
        this.priExecutor = Objects.requireNonNull(priExecutor, "Primary QueryExecutor cannot be null");
        this.secExecutor = Objects.requireNonNull(secExecutor, "Secondary QueryExecutor cannot be null");
        this.relationProcessors = List.of(
                new OneToOneLeftProcessor(this.secExecutor),
                new OneToOneRightProcessor(this.secExecutor),
                new OneToManyProcessor(this.secExecutor)
        );
    }

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        Objects.requireNonNull(info, "QueryInfo cannot be null");
        var priResult = priExecutor.executeQuery(info, args);
        if (priResult == null) {
            return null;
        }
        var entityType = info.getEntityType();
        for (var field : entityType.getDeclaredFields()) {
            for (var processor : relationProcessors) {
                if (processor.supports(field)) {
                    priResult = processor.correlate(field, info, priResult);
                }
            }
        }
        return priResult;
    }
}
