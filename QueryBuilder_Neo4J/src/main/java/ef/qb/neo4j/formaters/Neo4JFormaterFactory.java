package ef.qb.neo4j.formaters;

import ef.qb.core.annotation.ServicePriority;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.formater.DefaultParameterFormater;
import ef.qb.core.methodparser.formater.FormaterFactory;
import ef.qb.core.methodparser.formater.ParameterFormater;

@ServicePriority(1)
public class Neo4JFormaterFactory implements FormaterFactory {

    @Override
    public ParameterFormater getFormater(ComparisonType operator) {
        switch (operator) {
            default:
                return new DefaultParameterFormater();
        }
    }

}
