package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({long.class, Long.class})
public class ToLongConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Long.valueOf(value);
    }

}
