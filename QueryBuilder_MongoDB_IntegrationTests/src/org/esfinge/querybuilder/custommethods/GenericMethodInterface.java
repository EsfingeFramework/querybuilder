package org.esfinge.querybuilder.custommethods;

import org.esfinge.querybuilder.NeedClassConfiguration;

public interface GenericMethodInterface<E> extends NeedClassConfiguration<E> {
	
	public Class<E> getConfigClass();

}
