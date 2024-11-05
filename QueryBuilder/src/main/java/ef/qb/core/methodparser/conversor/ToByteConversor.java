package ef.qb.core.methodparser.conversor;

import ef.qb.core.annotation.ClassBasedService;

@ClassBasedService({byte.class, Byte.class})
public class ToByteConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Byte.valueOf(value);
    }

}
