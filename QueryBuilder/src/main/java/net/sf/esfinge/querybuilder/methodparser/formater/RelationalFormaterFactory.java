package net.sf.esfinge.querybuilder.methodparser.formater;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import static net.sf.esfinge.querybuilder.methodparser.ComparisonType.CONTAINS;
import static net.sf.esfinge.querybuilder.methodparser.ComparisonType.ENDS;
import static net.sf.esfinge.querybuilder.methodparser.ComparisonType.STARTS;

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
