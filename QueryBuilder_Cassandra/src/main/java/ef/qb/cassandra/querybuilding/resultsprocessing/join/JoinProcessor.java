package ef.qb.cassandra.querybuilding.resultsprocessing.join;

import ef.qb.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinUtils.filterListByJoinClause;
import java.util.List;

public class JoinProcessor extends BasicResultsProcessor {

    private final List<JoinClause> joinClauses;

    public JoinProcessor(List<JoinClause> specialComparisonClauses) {
        super();
        this.joinClauses = specialComparisonClauses;
    }

    public JoinProcessor(List<JoinClause> joinClauses, ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
        this.joinClauses = joinClauses;
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (joinClauses.isEmpty() || list.isEmpty())
            return list;

        for (var c : joinClauses) {
            list = filterListByJoinClause(list, c);
        }

        return list;
    }
}
