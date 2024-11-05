package ef.qb.core.utils;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

public class EsfingeELContext extends ELContext {

    private final FunctionMapper functionMapper;
    private final VariableMapper variableMapper;
    private final CompositeELResolver elResolver;

    public EsfingeELContext(FunctionMapper functionMapper, VariableMapper variableMapper, ELResolver... resolvers) {
        this.functionMapper = functionMapper;
        this.variableMapper = variableMapper;

        elResolver = new CompositeELResolver();

        for (var resolver : resolvers) {
            elResolver.add(resolver);
        }
    }

    @Override
    public ELResolver getELResolver() {
        return elResolver;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }

}
