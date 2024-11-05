package ef.qb.core.methodparser.conditions;

import java.util.List;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.formater.ParameterFormater;

public interface QueryCondition {

    public int getParameterSize();

    public List<String> getParameterNames();

    public List<String> getMethodParameterNames();

    public List<ParameterFormater> getParameterFormatters();

    public void visit(QueryVisitor visitor);

    public boolean isDynamic();

    public List<String> getMethodParameterProps();

}
