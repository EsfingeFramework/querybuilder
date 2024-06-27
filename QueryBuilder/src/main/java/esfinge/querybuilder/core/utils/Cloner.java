package esfinge.querybuilder.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Cloner {

    public static <T> T cloneObject(T original) {
        try {
            Class<?> clazz = original.getClass();
            T clone = (T) clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(original));
            }

            return clone;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException("Failed to clone object", e);
        }
    }
}
