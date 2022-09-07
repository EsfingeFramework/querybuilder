package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

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

        for (JoinClause c : joinClauses) {
            list = JoinUtils.filterListByJoinClause(list, c);
        }

        return list;
    }
}
