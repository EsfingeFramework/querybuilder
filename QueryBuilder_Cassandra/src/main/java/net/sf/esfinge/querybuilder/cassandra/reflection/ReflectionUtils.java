package net.sf.esfinge.querybuilder.cassandra.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    public static <E> Method[] getClassGetters(Class<E> clazz) {
        Method[] setters = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName()
                        .startsWith("get") && !m.getName().equals("getClass"))
                .toArray(Method[]::new);

        return setters;
    }

}


