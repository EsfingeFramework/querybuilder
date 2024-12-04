package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({int.class, Integer.class})
public class ToIntConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Integer.parseInt(value);
	}

}
