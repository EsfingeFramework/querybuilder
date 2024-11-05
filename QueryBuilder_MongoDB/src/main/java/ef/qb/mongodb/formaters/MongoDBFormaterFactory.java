package ef.qb.mongodb.formaters;

import ef.qb.core.annotation.ServicePriority;
import ef.qb.core.methodparser.ComparisonType;
import static ef.qb.core.methodparser.ComparisonType.CONTAINS;
import static ef.qb.core.methodparser.ComparisonType.ENDS;
import static ef.qb.core.methodparser.ComparisonType.STARTS;
import ef.qb.core.methodparser.formater.FormaterFactory;
import ef.qb.core.methodparser.formater.ParameterFormater;

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
