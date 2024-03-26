package net.sf.esfinge.querybuilder.methodparser.formater;

public class StartsParameterFormater implements ParameterFormater {

    @Override
    public Object formatParameter(Object param) {
        return param.toString() + "%";
    }
}
