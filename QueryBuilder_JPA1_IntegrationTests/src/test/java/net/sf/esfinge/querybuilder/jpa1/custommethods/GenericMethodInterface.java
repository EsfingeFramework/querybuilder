package net.sf.esfinge.querybuilder.jpa1.custommethods;

import net.sf.esfinge.querybuilder.NeedClassConfiguration;

public interface GenericMethodInterface<E> extends NeedClassConfiguration<E> {
	
	public Class<E> getConfigClass();

}
