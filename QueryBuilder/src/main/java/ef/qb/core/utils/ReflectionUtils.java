package ef.qb.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import ef.qb.core.annotation.ComparisonOperator;
import ef.qb.core.exception.InvalidPropertyException;
import ef.qb.core.exception.ParameterAnnotationNotFoundException;
import ef.qb.core.methodparser.ComparisonType;

public class ReflectionUtils {

    public static Class<?> getPropertyType(Class<?> c, String property) {
        try {
            var pointIndex = property.indexOf(".");
            String rest = null;
            if (pointIndex > 0) {
                rest = property.substring(pointIndex + 1);
                property = property.substring(0, pointIndex);
            }
            if (rest == null) {
                return getGetterReturnType(c, property);
            } else {
                return getPropertyType(getGetterReturnType(c, property), rest);
            }
        } catch (Exception e) {
            throw new InvalidPropertyException("The property " + property + " does not exist in class " + c.getName(), e);
        }
    }

    private static Class<?> getGetterReturnType(Class<?> c, String property) throws Exception {
        try {
            return c.getMethod("get" + StringUtils.firstCharWithUppercase(property)).getReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            return c.getMethod("is" + StringUtils.firstCharWithUppercase(property)).getReturnType();
        }
    }

    public static boolean existProperty(Class<?> c, String prop) {
        try {
            getPropertyType(c, prop);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isComparisonOperator(Class<? extends Annotation> an) {
        return an.isAnnotationPresent(ComparisonOperator.class);
    }

    public static Class<?> getFirstGenericTypeFromInterfaceImplemented(Class<?> baseInterf, Class<?> implInterf) {
        var superinterfaces = baseInterf.getGenericInterfaces();
        for (var type : superinterfaces) {
            var t = (ParameterizedType) type;
            if (t.getRawType() == implInterf && t.getActualTypeArguments().length > 0) {
                return (Class<?>) t.getActualTypeArguments()[0];
            }
        }
        return null;
    }

    public static boolean containsParameterAnnotation(Method m, Class<?> a) {
        for (var annotations : m.getParameterAnnotations()) {
            for (var annotation : annotations) {
                if (annotation.annotationType().getName().equals(a.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int getParameterAnnotationIndex(Method m, Class<?> a) {
        var annotations = m.getParameterAnnotations();
        for (var index = 0; index < annotations.length; index++) {
            for (var annotation : annotations[index]) {
                if (annotation.annotationType().getName().equals(a.getName())) {
                    return index;
                }
            }
        }

        throw new ParameterAnnotationNotFoundException("The method " + m.getName() + " has no parameter with " + a.getName() + " annotation");
    }

    public static boolean containsParameterAnnotation(Method method, int i, Class<?> annotation) {
        if (method.getParameterAnnotations().length == 0) {
            return false;
        }
        for (var an : method.getParameterAnnotations()[i]) {
            if (an.annotationType() == annotation) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGetter(Method m) {
        return m.getName().startsWith("get") && m.getParameterTypes().length == 0 && m.getReturnType() != void.class && !m.getName().equals("getClass");
    }

    public static ComparisonType getPropertyComparisonTypeByAnnotation(String propName, Class queryClass) {
        try {
            try {
                var f = queryClass.getDeclaredField(propName);
                for (var an : f.getAnnotations()) {
                    if (isComparisonOperator(an.annotationType())) {
                        return an.annotationType().getAnnotation(ComparisonOperator.class).value();
                    }
                }
            } catch (NoSuchFieldException | SecurityException e) {

            }
            var m = queryClass.getMethod(NameUtils.propertyToGetter(propName));
            for (var an : m.getAnnotations()) {
                if (isComparisonOperator(an.annotationType())) {
                    return an.annotationType().getAnnotation(ComparisonOperator.class).value();
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new InvalidPropertyException("Cannot access property " + propName + " on class " + queryClass.getName(), e);
        }
        return ComparisonType.EQUALS;
    }

    public static String getQueryParamenterName(Method getter) {
        var propName = NameUtils.acessorToProperty(getter.getName());
        if (NameUtils.endsWithComparisonOperator(propName)) {
            return propName;
        } else {
            return propName + getPropertyComparisonTypeByAnnotation(propName, getter.getDeclaringClass()).getOpName();
        }
    }

    public static Map<String, Object> toParameterMap(Object queryObj) {
        var map = new HashMap<String, Object>();
        for (var m : queryObj.getClass().getMethods()) {
            if (isGetter(m)) {
                Object value;
                try {
                    value = m.invoke(queryObj);
                    map.put(getQueryParamenterName(m), value);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException("Fail to create the parameter map", e);
                }
            }
        }
        return map;
    }

}
