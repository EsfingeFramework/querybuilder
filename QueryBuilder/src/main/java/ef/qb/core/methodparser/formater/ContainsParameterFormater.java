package ef.qb.core.methodparser.formater;

public class ContainsParameterFormater implements ParameterFormater {

    @Override
    public Object formatParameter(Object param) {
        return "%" + param.toString() + "%";
    }

}
