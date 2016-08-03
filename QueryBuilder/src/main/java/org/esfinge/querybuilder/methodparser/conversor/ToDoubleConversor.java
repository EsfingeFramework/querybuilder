package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({double.class, Double.class})
public class ToDoubleConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Double.parseDouble(value);
	}

}
