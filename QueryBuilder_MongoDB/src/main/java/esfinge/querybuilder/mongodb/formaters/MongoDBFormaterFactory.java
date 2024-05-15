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
        return switch (operator) {
            case CONTAINS ->
                new ContainsParameterFormater();
            case STARTS ->
                new StartsParameterFormater();
            case ENDS ->
                new EndsParameterFormater();
            default ->
                new DefaultParameterFormater();
        };
    }

}
