package net.sf.esfinge.querybuilder.methodparser.conversor;

import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class ConversorFactory {
	
	public static FromStringConversor get(Class c){
		return ServiceLocator.getServiceByRelatedClass(FromStringConversor.class, c);
	}

}
