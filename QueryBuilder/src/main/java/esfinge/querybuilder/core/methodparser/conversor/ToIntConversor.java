package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({int.class, Integer.class})
public class ToIntConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Integer.valueOf(value);
    }

}
