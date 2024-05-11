package esfinge.querybuilder.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.EntityClassProvider;
import esfinge.querybuilder.core.methodparser.MethodParser;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.SelectorMethodParser;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import esfinge.querybuilder.core.utils.ServiceLocator;

public class QueryBuilder implements InvocationHandler {

    //Static Part
    private static MethodParser configuredMethodParser;
    private static QueryExecutor configuredQueryExecutor;

    private static final Map<Class, Object> cachedProxies = new HashMap<>();
    private static final Map<Method, QueryInfo> queryInfoCache = new HashMap<>();

    public static void clearQueryInfoCache() {
        queryInfoCache.clear();
    }

    public static MethodParser getConfiguredMethodParser(Class<?> interf) {
        if (configuredMethodParser == null) {
            var mp = new SelectorMethodParser();
            mp.setInterface(interf);
            mp.setEntityClassProvider(ServiceLocator.getServiceImplementation(EntityClassProvider.class));
            return mp;
        }
        return configuredMethodParser;
    }

    public static void configureMethodParser(MethodParser mp) {
        configuredMethodParser = mp;
    }

    public static QueryExecutor getConfiguredQueryExecutor() {
        if (configuredQueryExecutor == null) {
            configuredQueryExecutor = ServiceLocator.getServiceImplementation(QueryExecutor.class);
        }
        return configuredQueryExecutor;
    }

    public static void configureQueryExecutor(QueryExecutor qe) {
        configuredQueryExecutor = qe;
    }

    public static <E> E create(Class<E> interf) {
        if (cachedProxies.containsKey(interf)) {
            return (E) cachedProxies.get(interf);
        }
        var qb = new QueryBuilder();
        qb.setMethodParser(getConfiguredMethodParser(interf));
        qb.setQueryExecutor(getConfiguredQueryExecutor());

        for (Class superinterf : interf.getInterfaces()) {
            var impl = ServiceLocator.getServiceImplementation(superinterf);
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
    public Object invoke(Object obj, Method m, Object[] args)
            throws Throwable {
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
