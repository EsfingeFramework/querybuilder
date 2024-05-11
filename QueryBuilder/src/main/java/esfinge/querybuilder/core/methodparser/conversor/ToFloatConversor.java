package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({float.class, Float.class})
public class ToFloatConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Float.valueOf(value);
    }

}
