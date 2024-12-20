package ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery;

import ef.qb.cassandra.exceptions.MethodInvocationException;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class SecondaryQueryUtils {

    public static boolean reflectiveEquals(Object obj1, Object obj2) {
        if (!obj1.getClass().equals(obj2.getClass())) {
            return false;
        }

        var obj1Getters = getClassGetters(obj1.getClass());
        var obj2Getters = getClassGetters(obj2.getClass());

        for (var i = 0; i < obj1Getters.length; i++) {
            try {
                var result1 = obj1Getters[i].invoke(obj1);
                var result2 = obj2Getters[i].invoke(obj2);

                if (result1 == null) {
                    if (result2 != null) {
                        return false;
                    }
                }

                if (result2 == null) {
                    if (result1 != null) {
                        return false;
                    }
                }

                if (result1 != null && !result1.equals(result2)) {
                    return false;
                }

            } catch (Exception e) {
                throw new MethodInvocationException(e.getMessage());
            }
        }

        return true;
    }

    public static <E> List removeDuplicateElementsFromList(List<E> list) {
        List<E> result = new ArrayList<>();

        for (var e : list) {
            if (!isObjectInList(e, result)) {
                result.add(e);
            }
        }

        return result;
    }

    private static <E> boolean isObjectInList(E object, List<E> list) {
        for (var e : list) {
            if (reflectiveEquals(e, object)) {
                return true;
            }
        }

        return false;
    }

    public static <E> List fromListOfLists(List<List<E>> result) {
        return result.stream()
                .flatMap(List::stream)
                .collect(toList());
    }
}
