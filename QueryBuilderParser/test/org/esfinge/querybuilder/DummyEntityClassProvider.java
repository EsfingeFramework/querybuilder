package org.esfinge.querybuilder;

import org.esfinge.querybuilder.methodparser.EntityClassProvider;

public class DummyEntityClassProvider implements EntityClassProvider{

	@Override
	public Class<?> getEntityClass(String name) {
		return null;
	}

	

}
