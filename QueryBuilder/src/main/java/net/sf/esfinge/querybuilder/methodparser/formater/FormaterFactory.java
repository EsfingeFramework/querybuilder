package net.sf.esfinge.querybuilder.methodparser.formater;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public interface FormaterFactory {

    ParameterFormater getFormater(ComparisonType operator);

}
