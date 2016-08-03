package org.esfinge.querybuilder.neo4j;

import java.lang.reflect.Method;

public class Neo4JDAOUtils {
	
	public static boolean isGetter(Method m) {
        return (m.getName().startsWith("get") || m.getName().startsWith("is"))
                && m.getParameterTypes().length == 0;
    }

    public static boolean isGetter(Method m, Class<?> c)
            throws SecurityException {
        if (!isGetter(m)) {
            return false;
        }
        return true;
        /*
        Field field = null;
        try {
            field = c.getDeclaredField(m.getName().substring(3, 4)
                    .toLowerCase()
                    + m.getName().substring(4));
        } catch (NoSuchFieldException ex) {
            //return !m.isAnnotationPresent(Transient.class);
        }

        //return !field.isAnnotationPresent(Transient.class)
              //  && !m.isAnnotationPresent(Transient.class);
        return false;
        */
    }

}
