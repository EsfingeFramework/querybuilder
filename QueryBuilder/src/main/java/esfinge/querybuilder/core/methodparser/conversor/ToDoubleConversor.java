package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.annotation.ClassBasedService;

@ClassBasedService({double.class, Double.class})
public class ToDoubleConversor implements FromStringConversor {

    @Override
    public Object convert(String value) {
        return Double.valueOf(value);
    }

}
