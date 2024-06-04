package esfinge.querybuilder.mongodb.formaters;

import esfinge.querybuilder.core.annotation.ServicePriority;
import esfinge.querybuilder.core.methodparser.ComparisonType;
import static esfinge.querybuilder.core.methodparser.ComparisonType.CONTAINS;
import static esfinge.querybuilder.core.methodparser.ComparisonType.ENDS;
import static esfinge.querybuilder.core.methodparser.ComparisonType.STARTS;
import esfinge.querybuilder.core.methodparser.formater.FormaterFactory;
import esfinge.querybuilder.core.methodparser.formater.ParameterFormater;

@ServicePriority(1)
public class MongoDBFormaterFactory implements FormaterFactory {

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
