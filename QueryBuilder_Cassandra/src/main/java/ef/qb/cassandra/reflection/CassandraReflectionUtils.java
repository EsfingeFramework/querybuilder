package ef.qb.cassandra.reflection;

import ef.qb.cassandra.exceptions.GetterNotFoundInClassException;
import java.lang.reflect.Method;
import static java.util.Arrays.stream;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class CassandraReflectionUtils {

    public static <E> Method[] getClassGetters(Class<E> clazz) {
        var fieldNames = stream(clazz.getDeclaredFields())
                .map(field -> field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1))
                .collect(toList());

        return stream(clazz.getMethods())
                .filter(m -> m.getName()
                .startsWith("get") && !m.getName().equals("getClass") && fieldNames.contains(m.getName().substring(3)))
                .toArray(Method[]::new);
    }

    public static <E> Method[] getClassGettersForFields(Class<E> clazz, List<String> fieldNames) {
        var getters = getClassGetters(clazz);

        // Need to order the methods with the same ordering as the fieldNames,
        // otherwise on runtime the chain ordering for multiple fields might change,
        // see ChainComparator class
        return fieldNames.stream()
                .map(name -> getClassGetterForField(clazz, getters, name))
                .toArray(Method[]::new);
    }

    public static <E> Method getClassGetterForField(Class<E> clazz, Method[] methods, String fieldName) {
        var getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        Method getter = null;

        for (var m : methods) {
            if (m.getName().equals(getterName)) {
                getter = m;
            }
        }

        if (getter == null) {
            throw new GetterNotFoundInClassException("Getter for field \"" + fieldName + "\" not found in class " + clazz.getSimpleName());
        }

        return getter;
    }

}
