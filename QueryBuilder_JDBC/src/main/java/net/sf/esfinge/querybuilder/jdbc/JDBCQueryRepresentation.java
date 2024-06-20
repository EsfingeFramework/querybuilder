package net.sf.esfinge.querybuilder.jdbc;

import java.util.Map;
import java.util.Set;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;

public class JDBCQueryRepresentation implements QueryRepresentation {

    private final String jdbcQuery;
    private final boolean dynamic;
    private final Map<String, Object> fixParameters;
    private final JDBCQueryVisitor jdbcVisitor;

    public JDBCQueryRepresentation(String jdbcQuery, boolean dynamic,
            Map<String, Object> fixParameters,
            JDBCQueryVisitor jdbcVisitor) {
        this.jdbcQuery = jdbcQuery;
        this.dynamic = dynamic;
        this.fixParameters = fixParameters;
        this.jdbcVisitor = jdbcVisitor;
    }

    @Override
    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public Object getQuery() {
        return jdbcQuery;
    }

    @Override
    public Object getQuery(Map<String, Object> params) {
        var isFirst = true;
        @SuppressWarnings("UnusedAssignment")
        var stQuery = new StringBuilder();
        stQuery = jdbcVisitor.getOriginalStQuery();

        stQuery.delete(0, stQuery.length());
        stQuery.append("select ");
        stQuery.append(jdbcVisitor.getSqlUtils().getFieldsEntities());
        stQuery.append(" from ");
        stQuery.append(jdbcVisitor.getSqlUtils().getChildEntities());

        for (var dynamicVar : jdbcVisitor.getListOfDynamicsObjetcs()) {
            dynamicVar.updateValues(params);
            if (dynamicVar.isAddField()) {
                if (isFirst) {
                    stQuery.append(" where ");
                    isFirst = false;
                } else {
                    stQuery.append(" and ");
                }
                stQuery.append(dynamicVar.getPropertyName()).append(" ");
                stQuery.append(dynamicVar.getOperator()).append(" ");
                stQuery.append(dynamicVar.getValueOfProperty());
            }

        }
        if (jdbcVisitor.getSqlUtils().haveJoinColumn()) {
            if (stQuery.toString().contains("where")) {
                stQuery.append(" and ");
            } else {
                stQuery.append(" where ");
            }
            stQuery.append(jdbcVisitor.getSqlUtils().getJoinExpressions());

        }
        return jdbcVisitor.getStQuery();
    }

    @Override
    public Set<String> getFixParameters() {
        return fixParameters.keySet();
    }

    @Override
    public Object getFixParameterValue(String param) {
        return fixParameters.get(param);
    }

}
