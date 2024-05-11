package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({byte.class, Byte.class})
public class ToByteConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Byte.valueOf(value);
    }

}
