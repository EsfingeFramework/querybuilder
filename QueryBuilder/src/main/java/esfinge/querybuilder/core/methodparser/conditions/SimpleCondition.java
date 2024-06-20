package esfinge.querybuilder.core.methodparser.conditions;

import java.util.ArrayList;
import java.util.List;
import esfinge.querybuilder.core.methodparser.ComparisonType;
import esfinge.querybuilder.core.methodparser.QueryVisitor;
import esfinge.querybuilder.core.methodparser.formater.FormaterFactory;
import esfinge.querybuilder.core.methodparser.formater.ParameterFormater;
import esfinge.querybuilder.core.utils.ServiceLocator;
import esfinge.querybuilder.core.utils.StringUtils;

public class SimpleCondition implements QueryCondition {

    private String name;
    private ComparisonType operator = ComparisonType.EQUALS;
    private NullOption nullOption = NullOption.NONE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComparisonType getOperator() {
        return operator;
    }

    public void setOperator(ComparisonType operator) {
        this.operator = operator;
    }

    public NullOption getNullOption() {
        return nullOption;
    }

    public void setNullOption(NullOption nullOption) {
        this.nullOption = nullOption;
    }

    @Override
    public int getParameterSize() {
        return 1;
    }

    @Override
    public List<String> getParameterNames() {
        List<String> l = new ArrayList<>();
        l.add(name);
        return l;
    }

    @Override
    public List<String> getMethodParameterNames() {
        List<String> l = new ArrayList<>();
        l.add(StringUtils.toCamelCase(name) + operator.getOpName());
        return l;
    }

    @Override
    public void visit(QueryVisitor visitor) {
        if (isDynamic()) {
            visitor.visitCondition(name, operator, nullOption);
        } else {
            visitor.visitCondition(name, operator);
        }
    }

    @Override
    public List<ParameterFormater> getParameterFormatters() {
        List<ParameterFormater> formatters = new ArrayList<>();
        var factory = ServiceLocator.getServiceImplementation(FormaterFactory.class);
        formatters.add(factory.getFormater(operator));
        return formatters;
    }

    @Override
    public boolean isDynamic() {
        return nullOption != NullOption.NONE;
    }

    @Override
    public List<String> getMethodParameterProps() {
        List<String> l = new ArrayList<>();
        l.add(name);
        return l;
    }

}
