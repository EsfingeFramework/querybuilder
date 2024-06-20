package esfinge.querybuilder.core.methodparser.conversor;

import esfinge.querybuilder.core.utils.ServiceLocator;

public class ConversorFactory {

    public static FromStringConversor get(Class c) {
        return ServiceLocator.getServiceByRelatedClass(FromStringConversor.class, c);
    }

}
