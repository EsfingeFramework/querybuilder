package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({long.class, Long.class})
public class ToLongConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Long.parseLong(value);
	}

}
