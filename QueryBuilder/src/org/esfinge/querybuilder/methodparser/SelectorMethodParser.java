package org.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;
import java.util.List;

import org.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import org.esfinge.querybuilder.utils.ServiceLocator;

public class SelectorMethodParser implements MethodParser{
	
	private List<MethodParser> parserList;
	private EntityClassProvider classProvider;
	
	public SelectorMethodParser() {
		parserList = ServiceLocator.getServiceImplementationList(MethodParser.class);
	}

	@Override
	public QueryInfo parse(Method m) {
		for(MethodParser mp : parserList){
			if(mp.fitParserConvention(m)){
				return mp.parse(m);
			}
		}
		throw new InvalidQuerySequenceException("The method "+m.getModifiers()+" does not fit in any possible QueryBuilder convention for query methods");
	}

	@Override
	public void setEntityClassProvider(EntityClassProvider classProvider) {
		this.classProvider = classProvider;
		for(MethodParser mp : parserList){
			mp.setEntityClassProvider(classProvider);
		}
	}

	@Override
	public boolean fitParserConvention(Method m) {
		for(MethodParser mp : parserList){
			if(mp.fitParserConvention(m)){
				return true;
			}
		}
		return false;
	}

	public void setInterface(Class<?> interf) {
		for(MethodParser mp : parserList){
			mp.setInterface(interf);
		}
	}
	
	// adding this only for the recognition of domain terms by the plugin
	// this does not imply any change in the main flow
	public void setInterface(Class<?> interf, ClassLoader loader) {
		for(MethodParser mp : parserList){
			mp.setInterface(interf, loader);
		}
	}
	
	public EntityClassProvider getEntityClassProvider() {
		return classProvider;
	}
	
	

}
