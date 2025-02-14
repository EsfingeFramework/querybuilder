package ef.qb.core;

import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.TargetEntity;
import ef.qb.core.executor.CompositeQueryExecutor;
import ef.qb.core.executor.QueryExecutor;
import ef.qb.core.methodparser.MethodParser;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.SelectorMethodParser;
import ef.qb.core.repository.CompositeRepository;
import ef.qb.core.utils.Cloner;
import ef.qb.core.utils.ReflectionUtils;
import ef.qb.core.utils.ServiceLocator;
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
        return getConfiguredQueryExecutorWithHP(implementationName);
    }

    private static QueryExecutor getConfiguredQueryExecutorWithHP(String implementationName) {
        var maxPriority = Integer.MIN_VALUE;
        QueryExecutor result = null;
        for (String key : configuredQueryExecutors.keySet()) {
            String[] parts = key.split("_priority=");
            if (parts.length == 2) {
                try {
                    var name = parts[0];
                    var priority = Integer.parseInt(parts[1]);
                    if (name.toLowerCase().equals(implementationName.toLowerCase())) {
                        if (priority > maxPriority) {
                            maxPriority = priority;
                            result = configuredQueryExecutors.get(key);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid priority value for key: " + key);
                }
            }
        }
        return result;
    }

    public static void configureQueryExecutors(Map<String, QueryExecutor> qes) {
        configuredQueryExecutors = qes;
    }

    public static NeedClassConfiguration getConfiguredClassConfig(Class superinterf, String implementationName) {
        if (configuredClassConfigs == null) {
            configuredClassConfigs = ServiceLocator.getServiceImplementationMap(superinterf);
        }
        return getConfiguredClassConfigWithHP(implementationName);
    }

    private static NeedClassConfiguration getConfiguredClassConfigWithHP(String implementationName) {
        var maxPriority = Integer.MIN_VALUE;
        NeedClassConfiguration result = null;
        for (String key : configuredClassConfigs.keySet()) {
            String[] parts = key.split("_priority=");
            if (parts.length == 2) {
                try {
                    var name = parts[0];
                    var priority = Integer.parseInt(parts[1]);
                    if (name.toLowerCase().equals(implementationName.toLowerCase())) {
                        if (priority > maxPriority) {
                            maxPriority = priority;
                            result = configuredClassConfigs.get(key);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid priority value for key: " + key);
                }
            }
        }
        return result;
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
            var priImplCloned = Cloner.cloneObject(priImpl);
            if (priImplCloned != null) {

                Class<?> clazz = null;

                if (NeedClassConfiguration.class.isAssignableFrom(superinterf)) {
                    clazz = ReflectionUtils.getFirstGenericTypeFromInterfaceImplemented(interf, superinterf);
                    priImplCloned.configureClass(clazz);
                }

                var secImpl = getConfiguredClassConfig(superinterf, persistenceConfig.secondary);
                NeedClassConfiguration secImplCloned = null;
                if (secImpl != null) {
                    secImplCloned = Cloner.cloneObject(secImpl);
                }
                if (secImplCloned == null) {
                    qb.addImplementation(superinterf, priImplCloned);
                } else if (isRepository(priImplCloned.getClass()) && isRepository(secImplCloned.getClass())) {
                    var composite = new CompositeRepository<E>(
                            Repository.class.cast(priImplCloned),
                            Repository.class.cast(secImplCloned),
                            qb.getQueryExecutor());
                    ((NeedClassConfiguration) composite).configureClass(clazz);
                    qb.addImplementation(interf, composite);
                } else {
                    throw new RuntimeException(priImplCloned + " and " + secImplCloned + " must be <Reposity> implementations.");
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
        for (var iface : implementation.getInterfaces()) {
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
