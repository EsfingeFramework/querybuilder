package esfinge.querybuilder.jpa1;

import esfinge.querybuilder.core.methodparser.QueryRepresentation;
import esfinge.querybuilder.core.utils.ELUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JPAQueryRepresentation implements QueryRepresentation {

    private final String jpaQuery;
    private final boolean dynamic;
    private final Map<String, Object> fixParameters;

    public JPAQueryRepresentation(String jpaQuery, boolean dynamic,
            Map<String, Object> fixParameters) {
        this.jpaQuery = jpaQuery;
        this.dynamic = dynamic;
        this.fixParameters = fixParameters;
    }

    @Override
    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public Object getQuery() {
        return jpaQuery;
    }

    @Override
    public Object getQuery(Map<String, Object> params) {
        Map<String, Method> funcMap = new HashMap<>();
        for (var m : ELFunctions.class.getMethods()) {
            funcMap.put(m.getName(), m);
        }
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("map", params);
        for (var key : params.keySet()) {
            propMap.put(key, params.get(key));
        }
        var ctx = ELUtils.buildEvaluationContext(funcMap, propMap);

        return ELUtils.evaluateExpression(ctx, getQuery().toString());
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
