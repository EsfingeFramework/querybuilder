package ef.qb.mongodb.formaters;

import ef.qb.core.methodparser.formater.ParameterFormater;

public class EndsParameterFormater implements ParameterFormater {

    @Override
    public Object formatParameter(Object param) {
        return ".*" + param.toString();
    }

}
