package ef.qb.core.methodparser.conversor;

import ef.qb.core.annotation.ClassBasedService;

@ClassBasedService({float.class, Float.class})
public class ToFloatConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Float.valueOf(value);
    }

}
