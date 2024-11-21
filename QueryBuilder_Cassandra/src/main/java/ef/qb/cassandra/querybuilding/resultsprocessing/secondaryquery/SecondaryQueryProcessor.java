package ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery;

import ef.qb.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryUtils.removeDuplicateElementsFromList;
import java.util.List;

public class SecondaryQueryProcessor extends BasicResultsProcessor {

    public SecondaryQueryProcessor() {
    }

    public SecondaryQueryProcessor(ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (list.isEmpty()) {
            return list;
        }

        return removeDuplicateElementsFromList(list);
    }

}
