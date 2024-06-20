package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({short.class, Short.class})
public class ToShortConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Short.valueOf(value);
    }

}
