package ef.qb.core.methodparser.conversor;

import ef.qb.core.annotation.ClassBasedService;

@ClassBasedService({double.class, Double.class})
public class ToDoubleConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Double.valueOf(value);
    }

}
