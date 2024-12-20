package ef.qb.mongodb;

import dev.morphia.annotations.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MongoDBDAOUtils {

    public static boolean isGetter(Method m) {
        return (m.getName().startsWith("get") || m.getName().startsWith("is"))
                && m.getParameterTypes().length == 0;
    }

    public static boolean isGetterWhichIsNotTransient(Method m, Class<?> c)
            throws SecurityException {
        if (!isGetter(m)) {
            return false;
        }
        Field field = null;
        try {
            field = c.getDeclaredField(m.getName().substring(3, 4)
                    .toLowerCase()
                    + m.getName().substring(4));
        } catch (NoSuchFieldException ex) {
            return !m.isAnnotationPresent(Transient.class);
        }

        return !field.isAnnotationPresent(Transient.class)
                && !m.isAnnotationPresent(Transient.class);
    }

}
