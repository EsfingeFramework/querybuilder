package org.esfinge.querybuilder.jpa1.custommethods;

import org.esfinge.querybuilder.NeedClassConfiguration;

public interface GenericMethodInterface<E> extends NeedClassConfiguration<E> {
	
	public Class<E> getConfigClass();

}
