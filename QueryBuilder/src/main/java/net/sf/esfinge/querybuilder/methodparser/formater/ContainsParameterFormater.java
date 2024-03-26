package net.sf.esfinge.querybuilder.methodparser.formater;

public class ContainsParameterFormater implements ParameterFormater {

    @Override
    public Object formatParameter(Object param) {
        return "%" + param.toString() + "%";
    }

}
