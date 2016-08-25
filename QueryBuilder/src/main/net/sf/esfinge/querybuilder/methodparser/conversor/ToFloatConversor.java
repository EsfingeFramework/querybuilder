package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.annotation.ClassBasedService;


@ClassBasedService({float.class, Float.class})
public class ToFloatConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Float.parseFloat(value);
	}

}
