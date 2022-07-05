package net.sf.esfinge.querybuilder.cassandra.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    public static <E> Method[] getClassSetters(Class<E> clazz) {
        Method setters[] = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName()
                        .startsWith("set"))
                .toArray(Method[]::new);

        return setters;
    }
}


