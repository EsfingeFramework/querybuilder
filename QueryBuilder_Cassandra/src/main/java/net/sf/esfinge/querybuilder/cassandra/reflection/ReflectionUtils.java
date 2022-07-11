package net.sf.esfinge.querybuilder.cassandra.reflection;

import net.sf.esfinge.querybuilder.cassandra.exceptions.FieldNotFoundInClassException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    public static <E> Method[] getClassGetters(Class<E> clazz) {
        Method[] setters = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName()
                        .startsWith("get") && !m.getName().equals("getClass"))
                .toArray(Method[]::new);

        return setters;
    }

    public static <E> Method[] getClassGettersForFields(Class<E> clazz, List<String> fieldNames){
        Method[] getters = getClassGetters(clazz);

        Method[] gettersForFields = Arrays.stream(getters)
                .filter(g -> fieldNames.contains(g.getName().substring(3,4).toLowerCase() + g.getName().substring(4)))
                .toArray(Method[]::new);

        if (gettersForFields.length != fieldNames.size())
            throw new FieldNotFoundInClassException("One of these fields is not present in the class " + clazz.getSimpleName() + ": " + Arrays.toString(fieldNames.toArray()));

        return gettersForFields;
    }

}


