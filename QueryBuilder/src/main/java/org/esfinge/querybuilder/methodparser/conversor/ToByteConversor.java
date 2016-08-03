package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({byte.class, Byte.class})
public class ToByteConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Byte.parseByte(value);
	}

}
