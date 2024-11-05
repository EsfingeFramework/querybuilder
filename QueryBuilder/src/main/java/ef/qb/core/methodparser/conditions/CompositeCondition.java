package ef.qb.core.methodparser.conditions;

import java.util.List;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.formater.ParameterFormater;

public class CompositeCondition implements QueryCondition {

    private final QueryCondition firstCondition;
    private final QueryCondition secondCondition;
    private final String conector;

    public CompositeCondition(QueryCondition firstCondition, String conector,
            QueryCondition secondCondition) {
        super();
        this.firstCondition = firstCondition;
        this.conector = conector;
        this.secondCondition = secondCondition;
    }

    @Override
    public int getParameterSize() {
        return firstCondition.getParameterSize() + secondCondition.getParameterSize();
    }

    @Override
    public List<String> getParameterNames() {
        var result = firstCondition.getParameterNames();
        result.addAll(secondCondition.getParameterNames());
        return result;
    }

    @Override
    public List<String> getMethodParameterNames() {
        var result = firstCondition.getMethodParameterNames();
        result.addAll(secondCondition.getMethodParameterNames());
        return result;
    }

    @Override
    public void visit(QueryVisitor visitor) {
        firstCondition.visit(visitor);
        visitor.visitConector(conector);
        secondCondition.visit(visitor);
    }

    @Override
    public List<ParameterFormater> getParameterFormatters() {
        var result = firstCondition.getParameterFormatters();
        result.addAll(secondCondition.getParameterFormatters());
        return result;
    }

    @Override
    public boolean isDynamic() {
        return firstCondition.isDynamic() || secondCondition.isDynamic();
    }

    @Override
    public List<String> getMethodParameterProps() {
        var result = firstCondition.getMethodParameterProps();
        result.addAll(secondCondition.getMethodParameterProps());
        return result;
    }

}
