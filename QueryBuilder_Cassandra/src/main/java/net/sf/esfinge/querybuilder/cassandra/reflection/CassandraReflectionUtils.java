package net.sf.esfinge.querybuilder.cassandra.reflection;

import net.sf.esfinge.querybuilder.cassandra.exceptions.GetterNotFoundInClassException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class CassandraReflectionUtils {

    public static <E> Method[] getClassGetters(Class<E> clazz) {

        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName()
                        .startsWith("get") && !m.getName().equals("getClass"))
                .toArray(Method[]::new);
    }

    public static <E> Method[] getClassGettersForFields(Class<E> clazz, List<String> fieldNames) {
        Method[] getters = getClassGetters(clazz);

        // Need to order the methods with the same ordering as the fieldNames,
        // otherwise on runtime the chain ordering for multiple fields might change,
        // see ChainComparator class
        return fieldNames.stream()
                .map(name -> getClassGetterForField(clazz, getters, name))
                .toArray(Method[]::new);
    }

    public static <E> Method getClassGetterForField(Class<E> clazz, Method[] methods, String fieldName) {
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        Method getter = null;

        for (Method m : methods) {
            if (m.getName().equals(getterName))
                getter = m;
        }

        if (getter == null)
            throw new GetterNotFoundInClassException("Getter for field " + fieldName + " not found in class " + clazz.getSimpleName());

        return getter;
    }

}


