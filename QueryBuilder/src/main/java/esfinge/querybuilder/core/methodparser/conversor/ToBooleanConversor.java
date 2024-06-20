package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({boolean.class, Boolean.class})
public class ToBooleanConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Boolean.valueOf(value);
    }

}
