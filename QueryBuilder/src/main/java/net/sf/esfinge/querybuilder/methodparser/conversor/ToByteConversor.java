package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({byte.class, Byte.class})
public class ToByteConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Byte.parseByte(value);
	}

}
