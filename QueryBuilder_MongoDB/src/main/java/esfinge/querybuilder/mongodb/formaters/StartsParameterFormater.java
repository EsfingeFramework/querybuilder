package esfinge.querybuilder.mongodb.formaters;

import esfinge.querybuilder.core.methodparser.formater.ParameterFormater;

public class StartsParameterFormater implements ParameterFormater {

    @Override
    public Object formatParameter(Object param) {
        return param.toString() + ".*";
    }
}
