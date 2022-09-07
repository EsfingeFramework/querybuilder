package net.sf.esfinge.querybuilder.cassandra.integration.custommethods;

import net.sf.esfinge.querybuilder.NeedClassConfiguration;

public interface GenericMethodInterface<E> extends NeedClassConfiguration<E> {
	
	public Class<E> getConfigClass();

}
