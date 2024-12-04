package org.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;

public interface MethodParser {

	public abstract QueryInfo parse(Method m);

	public abstract void setEntityClassProvider(EntityClassProvider classProvider);
	
	public boolean fitParserConvention(Method m);

	public abstract void setInterface(Class<?> interf);

}