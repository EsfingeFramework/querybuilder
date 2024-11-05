package ef.qb.core.methodparser.conversor;

import ef.qb.core.annotation.ClassBasedService;

@ClassBasedService({short.class, Short.class})
public class ToShortConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Short.valueOf(value);
    }

}
