package org.esfinge.querybuilder.methodparser.conversor;

import org.esfinge.querybuilder.utils.ServiceLocator;

public class ConversorFactory {
	
	public static FromStringConversor get(Class c){
		return ServiceLocator.getServiceByRelatedClass(FromStringConversor.class, c);
	}

}
