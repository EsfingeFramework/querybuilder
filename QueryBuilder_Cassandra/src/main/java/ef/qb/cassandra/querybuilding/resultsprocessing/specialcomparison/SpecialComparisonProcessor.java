package ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison;

import ef.qb.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils.filterListBySpecialComparisonClause;
import java.util.List;

public class SpecialComparisonProcessor extends BasicResultsProcessor {

    private final List<SpecialComparisonClause> specialComparisonClauses;

    public SpecialComparisonProcessor(List<SpecialComparisonClause> specialComparisonClauses) {
        super();
        this.specialComparisonClauses = specialComparisonClauses;
    }

    public SpecialComparisonProcessor(List<SpecialComparisonClause> specialComparisonClauses, ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
        this.specialComparisonClauses = specialComparisonClauses;
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (specialComparisonClauses.isEmpty() || list.isEmpty()) {
            return list;
        }

        for (SpecialComparisonClause c : specialComparisonClauses) {
            list = filterListBySpecialComparisonClause(list, c);
        }

        return list;
    }

}
