package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing;

import java.util.List;

public interface ResultsProcessor {

    <E> List<E> postProcess(List<E> list);

}
