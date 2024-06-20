package esfinge.querybuilder.core.methodparser.conditions;

import java.util.List;
import esfinge.querybuilder.core.methodparser.QueryVisitor;
import esfinge.querybuilder.core.methodparser.formater.ParameterFormater;

public interface QueryCondition {

    public int getParameterSize();

    public List<String> getParameterNames();

    public List<String> getMethodParameterNames();

    public List<ParameterFormater> getParameterFormatters();

    public void visit(QueryVisitor visitor);

    public boolean isDynamic();

    public List<String> getMethodParameterProps();

}
