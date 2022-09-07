package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

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
        if (specialComparisonClauses.isEmpty() || list.isEmpty())
            return list;

        for (SpecialComparisonClause c : specialComparisonClauses) {
            list = SpecialComparisonUtils.filterListBySpecialComparisonClause(list, c);
        }

        return list;
    }


}
