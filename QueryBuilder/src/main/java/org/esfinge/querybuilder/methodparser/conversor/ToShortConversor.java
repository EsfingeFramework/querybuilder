package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({short.class, Short.class})
public class ToShortConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Short.parseShort(value);
	}

}