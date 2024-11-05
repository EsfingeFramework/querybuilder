package ef.qb.core.methodparser.conditions;

import java.util.ArrayList;
import java.util.List;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.formater.ParameterFormater;

public class NullCondition implements QueryCondition {

    @Override
    public int getParameterSize() {
        return 0;
    }

    @Override
    public List<String> getParameterNames() {
        return new ArrayList<>();
    }

    @Override
    public void visit(QueryVisitor visitor) {
    }

    @Override
    public List<ParameterFormater> getParameterFormatters() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getMethodParameterNames() {
        return new ArrayList<>();
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public List<String> getMethodParameterProps() {
        return new ArrayList<>();
    }

}
