package esfinge.querybuilder.core;

import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.annotation.TargetEntity;
import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.MethodParser;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.SelectorMethodParser;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import esfinge.querybuilder.core.utils.ServiceLocator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder implements InvocationHandler {

    //Static Part
    private static MethodParser configuredMethodParser;
    private static Map<String, QueryExecutor> configuredQueryExecutors;
    private static Map<String, NeedClassConfiguration> configuredClassConfigs;

    private static final Map<Class, Object> cachedProxies = new HashMap<>();
    private static final Map<Method, QueryInfo> queryInfoCache = new HashMap<>();

    public static void clearQueryInfoCache() {
        queryInfoCache.clear();
    }

    public static MethodParser getConfiguredMethodParser(Class<?> interf) {
        if (configuredMethodParser == null) {
            var mp = new SelectorMethodParser();
            mp.setInterface(interf);
            return mp;
        }
        return configuredMethodParser;
    }

    public static void configureMethodParser(MethodParser mp) {
        configuredMethodParser = mp;
    }

    public static QueryExecutor getConfiguredQueryExecutor(String implementationName) {
        if (configuredQueryExecutors == null) {
            configuredQueryExecutors = ServiceLocator.getServiceImplementationMap(QueryExecutor.class);
        }
        return configuredQueryExecutors.get(implementationName);
    }

    public static void configureQueryExecutors(Map<String, QueryExecutor> qes) {
        configuredQueryExecutors = qes;
    }

    public static NeedClassConfiguration getConfiguredClassConfig(Class superinterf, String implementationName) {
        if (configuredClassConfigs == null) {
            configuredClassConfigs = ServiceLocator.getServiceImplementationMap(superinterf);
        }
        return configuredClassConfigs.get(implementationName);
    }

    public static <E> E create(Class<E> interf) {
        if (cachedProxies.containsKey(interf)) {
            return (E) cachedProxies.get(interf);
        }

        var persistenceType = "";
        if (interf.isAnnotationPresent(TargetEntity.class)) {
            var entityClass = interf.getAnnotation(TargetEntity.class).value();
            if (entityClass != null) {
                if (entityClass.isAnnotationPresent(PersistenceType.class)) {
                    persistenceType = entityClass.getAnnotation(PersistenceType.class).value();
                } else {
                    System.out.println("There is no @PersistenceType annotation.");
                }
            } else {
                System.out.println("EntityClass in @TargetEntity is null.");
            }
        } else {
            System.out.println("There is no @TargetEntity annotation.");
        }

        var qb = new QueryBuilder();
        qb.setMethodParser(getConfiguredMethodParser(interf));
        qb.setQueryExecutor(getConfiguredQueryExecutor(persistenceType));

        for (Class superinterf : interf.getInterfaces()) {
            var impl = getConfiguredClassConfig(superinterf, persistenceType);
            if (impl != null) {
                qb.addImplementation(superinterf, impl);
                if (NeedClassConfiguration.class.isAssignableFrom(superinterf)) {
                    Class clazz = ReflectionUtils.getFirstGenericTypeFromInterfaceImplemented(interf, superinterf);
                    ((NeedClassConfiguration) impl).configureClass(clazz);
                }
            }
        }

        var proxy = (E) Proxy.newProxyInstance(interf.getClassLoader(), new Class[]{interf}, qb);
        cachedProxies.put(interf, proxy);
        return proxy;
    }

    public static void clearCache() {
        cachedProxies.clear();
    }

    //Concrete Part
    private MethodParser mp;
    private QueryExecutor qe;
    private final Map<Class, Object> implementedInterfaces = new HashMap<>();

    public void setMethodParser(MethodParser mp) {
        this.mp = mp;
    }

    public void setQueryExecutor(QueryExecutor qe) {
        this.qe = qe;
    }

    public void addImplementation(Class interf, Object obj) {
        implementedInterfaces.put(interf, obj);
    }

    @Override
    public Object invoke(Object obj, Method m, Object[] args) throws Throwable {
        for (var superinterf : implementedInterfaces.keySet()) {
            try {
                var customMethod = superinterf.getMethod(m.getName(), m.getParameterTypes());
                var target = implementedInterfaces.get(superinterf);
                return customMethod.invoke(target, args);
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
        if (!queryInfoCache.containsKey(m)) {
            queryInfoCache.put(m, mp.parse(m));
        }
        return qe.executeQuery(queryInfoCache.get(m), args);
    }

}
