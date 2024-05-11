package esfinge.querybuilder.core.methodparser.formater;

import esfinge.querybuilder.core.methodparser.ComparisonType;

public interface FormaterFactory {

    ParameterFormater getFormater(ComparisonType operator);

}
