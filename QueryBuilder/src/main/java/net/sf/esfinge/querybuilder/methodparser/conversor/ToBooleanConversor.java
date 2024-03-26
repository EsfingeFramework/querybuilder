package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({boolean.class, Boolean.class})
public class ToBooleanConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Boolean.valueOf(value);
    }

}
