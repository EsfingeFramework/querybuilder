package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;

import java.util.Map;
import java.util.Set;

public class CassandraQueryRepresentation implements QueryRepresentation {

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public Object getQuery() {
        return null;
    }

    @Override
    public Object getQuery(Map<String, Object> map) {
        return null;
    }

    @Override
    public Set<String> getFixParameters() {
        return null;
    }

    @Override
    public Object getFixParameterValue(String s) {
        return null;
    }
}
