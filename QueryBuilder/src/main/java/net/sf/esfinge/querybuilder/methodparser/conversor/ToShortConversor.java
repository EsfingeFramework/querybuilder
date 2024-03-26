package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({short.class, Short.class})
public class ToShortConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Short.valueOf(value);
    }

}
