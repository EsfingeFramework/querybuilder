package ef.qb.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Cloner {

    public static <T> T cloneObject(T original) {
        try {
            var clazz = original.getClass();
            var clone = (T) clazz.getDeclaredConstructor().newInstance();
            for (var field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(original));
            }

            return clone;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException("Failed to clone object", e);
        }
    }
}
