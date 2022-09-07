package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

import java.util.List;

public class SecondaryQueryProcessor extends BasicResultsProcessor {

    public SecondaryQueryProcessor() {
    }

    public SecondaryQueryProcessor(ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        if (list.isEmpty())
            return list;

        return SecondaryQueryUtils.removeDuplicateElementsFromList(list);
    }

}
