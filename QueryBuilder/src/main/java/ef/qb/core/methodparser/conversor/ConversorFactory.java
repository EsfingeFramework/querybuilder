package ef.qb.core.methodparser.conversor;

import ef.qb.core.utils.ServiceLocator;

public class ConversorFactory {

    public static FromStringConversor get(Class c) {
        return ServiceLocator.getServiceByRelatedClass(FromStringConversor.class, c);
    }

}
