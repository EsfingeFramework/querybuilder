package ef.qb.core.methodparser.conversor;

import ef.qb.core.annotation.ClassBasedService;

@ClassBasedService({long.class, Long.class})
public class ToLongConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Long.valueOf(value);
    }

}
