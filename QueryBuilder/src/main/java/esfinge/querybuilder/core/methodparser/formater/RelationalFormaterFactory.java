package esfinge.querybuilder.core.methodparser.formater;

import esfinge.querybuilder.core.methodparser.ComparisonType;

public class RelationalFormaterFactory implements FormaterFactory {

    @Override
    public ParameterFormater getFormater(ComparisonType operator) {
        return switch (operator) {
            case CONTAINS -> new ContainsParameterFormater();
            case STARTS -> new StartsParameterFormater();
            case ENDS -> new EndsParameterFormater();
            default -> new DefaultParameterFormater();
        };
    }

}
