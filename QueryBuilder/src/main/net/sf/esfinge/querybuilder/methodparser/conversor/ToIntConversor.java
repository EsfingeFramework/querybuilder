package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.annotation.ClassBasedService;

@ClassBasedService({int.class, Integer.class})
public class ToIntConversor implements FromStringConversor {

	@Override
	public Object convert(String value) {
		return Integer.parseInt(value);
	}

}
