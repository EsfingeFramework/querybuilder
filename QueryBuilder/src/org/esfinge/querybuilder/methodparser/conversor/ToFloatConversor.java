package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;


@ClassBasedService({float.class, Float.class})
public class ToFloatConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Float.parseFloat(value);
	}

}
