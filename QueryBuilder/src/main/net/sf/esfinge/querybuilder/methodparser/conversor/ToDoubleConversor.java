package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({double.class, Double.class})
public class ToDoubleConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Double.parseDouble(value);
	}

}
