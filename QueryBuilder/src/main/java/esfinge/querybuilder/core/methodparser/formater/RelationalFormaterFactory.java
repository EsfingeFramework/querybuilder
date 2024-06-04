package esfinge.querybuilder.core.methodparser.formater;

import esfinge.querybuilder.core.methodparser.ComparisonType;

public class RelationalFormaterFactory implements FormaterFactory {

    @Override
    public ParameterFormater getFormater(ComparisonType operator) {
        switch (operator) {
            case CONTAINS:
                return new ContainsParameterFormater();
            case STARTS:
                return new StartsParameterFormater();
            case ENDS:
                return new EndsParameterFormater();
            default:
                return new DefaultParameterFormater();
        }
    }

}
