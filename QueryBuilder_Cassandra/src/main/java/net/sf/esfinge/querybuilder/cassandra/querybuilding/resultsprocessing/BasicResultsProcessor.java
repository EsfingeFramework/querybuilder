package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing;

import java.util.List;

public abstract class BasicResultsProcessor implements ResultsProcessor {
    private ResultsProcessor nextResultsProcessor;

    public BasicResultsProcessor() {
        super();
    }

    public BasicResultsProcessor(ResultsProcessor nextPostprocessor) {
        super();
        this.nextResultsProcessor = nextPostprocessor;
    }

    @Override
    public <E> List<E> postProcess(List<E> list) {
        list = resultsProcessing(list);

        if (nextResultsProcessor != null)
            list = nextResultsProcessor.postProcess(list);

        return list;
    }

    public abstract <E> List<E> resultsProcessing(List<E> list);
}
