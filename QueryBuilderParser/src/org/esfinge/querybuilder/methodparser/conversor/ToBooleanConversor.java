package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({boolean.class, Boolean.class})
public class ToBooleanConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Boolean.parseBoolean(value);
	}

}
