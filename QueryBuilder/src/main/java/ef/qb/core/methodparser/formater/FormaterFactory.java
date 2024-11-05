package ef.qb.core.methodparser.formater;

import ef.qb.core.methodparser.ComparisonType;

public interface FormaterFactory {

    ParameterFormater getFormater(ComparisonType operator);

}
