package esfinge.querybuilder.core;

import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.annotation.TargetEntity;
import esfinge.querybuilder.core.executor.CompositeQueryExecutor;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder implements InvocationHandler {

    // Static Part
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
            configuredMethodParser = new SelectorMethodParser();
            configuredMethodParser.setInterface(interf);
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
        return (E) cachedProxies.computeIfAbsent(interf, k -> {
            var qb = new QueryBuilder();
            var persistenceConfig = getPersistenceConfig(interf);
            qb.setMethodParser(getConfiguredMethodParser(interf));
            qb.setQueryExecutor(createQueryExecutor(persistenceConfig));
            configureImplementations(interf, qb, persistenceConfig);
            return Proxy.newProxyInstance(interf.getClassLoader(), new Class[]{interf}, qb);
        });
    }

    private static PersistenceConfig getPersistenceConfig(Class<?> interf) {
        if (interf.isAnnotationPresent(TargetEntity.class)) {
            var entityClass = interf.getAnnotation(TargetEntity.class).value();
            if (entityClass != null && entityClass.isAnnotationPresent(PersistenceType.class)) {
                var annotation = entityClass.getAnnotation(PersistenceType.class);
                return new PersistenceConfig(annotation.value(), annotation.secondary());
            }
        }
        System.out.println("There is no @TargetEntity or @PersistenceType annotation.");
        return new PersistenceConfig("", "");
    }

    private static QueryExecutor createQueryExecutor(PersistenceConfig config) {
        var primaryExecutor = getConfiguredQueryExecutor(config.primary);
        return config.secondary.equalsIgnoreCase("NONE")
                ? primaryExecutor
                : new CompositeQueryExecutor(primaryExecutor, getConfiguredQueryExecutor(config.secondary));
    }

    private static <E> void configureImplementations(Class<E> interf, QueryBuilder qb, PersistenceConfig persistenceConfig) {
        for (var superinterf : interf.getInterfaces()) {
            var priImpl = getConfiguredClassConfig(superinterf, persistenceConfig.primary);
            if (priImpl != null) {

                Class<?> clazz = null;

                if (NeedClassConfiguration.class.isAssignableFrom(superinterf)) {
                    clazz = ReflectionUtils.getFirstGenericTypeFromInterfaceImplemented(interf, superinterf);
                    priImpl.configureClass(clazz);
                }

                var secImpl = getConfiguredClassConfig(superinterf, persistenceConfig.secondary);
                if (secImpl == null) {
                    qb.addImplementation(superinterf, priImpl);
                } else if (isRepository(priImpl.getClass()) && isRepository(secImpl.getClass())) {
                    var composite = new CompositeRepository<E>(
                            Repository.class.cast(priImpl),
                            Repository.class.cast(secImpl),
                            qb.getQueryExecutor());
                    ((NeedClassConfiguration) composite).configureClass(clazz);
                    qb.addImplementation(interf, composite);
                } else {
                    throw new RuntimeException(priImpl + " and " + secImpl + " must be <Reposity> implementations.");
                }
            }
        }
    }

    public static void clearCache() {
        cachedProxies.clear();
    }

    private static boolean isRepository(Class<?> implementation) {
        if (implementation == null) {
            return false;
        }
        if (Arrays.stream(implementation.getInterfaces()).anyMatch(i -> i.equals(Repository.class))) {
            return true;
        }
        for (Class<?> iface : implementation.getInterfaces()) {
            if (isRepository(iface)) {
                return true;
            }
        }
        return isRepository(implementation.getSuperclass());
    }

    //Concrete Part
    private MethodParser mp;
    private QueryExecutor qe;
    private final Map<Class, Object> implementedInterfaces = new HashMap<>();

    public void setMethodParser(MethodParser mp) {
        this.mp = mp;
    }

    public QueryExecutor getQueryExecutor() {
        return qe;
    }

    public void setQueryExecutor(QueryExecutor qe) {
        this.qe = qe;
    }

    public void addImplementation(Class interf, Object obj) {
        implementedInterfaces.put(interf, obj);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (var superinterf : implementedInterfaces.keySet()) {
            try {
                var customMethod = superinterf.getMethod(method.getName(), method.getParameterTypes());
                return customMethod.invoke(implementedInterfaces.get(superinterf), args);
            } catch (NoSuchMethodException | IllegalArgumentException ignored) {
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
        queryInfoCache.computeIfAbsent(method, mp::parse);
        return qe.executeQuery(queryInfoCache.get(method), args);
    }

    private static class PersistenceConfig {

        String primary;
        String secondary;

        PersistenceConfig(String primary, String secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }
    }
}
