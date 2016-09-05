package net.sf.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;

public interface MethodParser {

	public abstract QueryInfo parse(Method m);

	public abstract void setEntityClassProvider(EntityClassProvider classProvider);
	
	public boolean fitParserConvention(Method m);

	public abstract void setInterface(Class<?> interf);
	
	// adding this only for the recognition of domain terms by the plugin
	// this does not imply any change in the main flow
	public abstract void setInterface(Class<?> interf, ClassLoader loader);

}