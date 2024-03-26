package net.sf.esfinge.querybuilder.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.sf.esfinge.querybuilder.annotation.ClassBasedService;
import net.sf.esfinge.querybuilder.annotation.ServicePriority;

public class ServiceLocator {

    private static final Map<Class, Map<Class, Object>> classRelatedServices = new HashMap<>();

    public static <E> E getServiceImplementation(Class<E> interf) {
        var loader = ServiceLoader.load(interf);
        var it = loader.iterator();
        E current = null;
        var currentPriority = -1;
        while (it.hasNext()) {
            var item = it.next();
            var objPriority = getObjectPriority(item);
            if (objPriority > currentPriority) {
                current = item;
                currentPriority = objPriority;
            }
        }
        return current;
    }

    public static <E> List<E> getServiceImplementationList(Class<E> interf) {
        var loader = ServiceLoader.load(interf);
        var it = loader.iterator();
        List<E> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        Collections.sort(list, new PriorityComparator());
        return list;
    }

    public static int getObjectPriority(Object obj) {
        if (!obj.getClass().isAnnotationPresent(ServicePriority.class)) {
            return 0;
        }
        return obj.getClass().getAnnotation(ServicePriority.class).value();
    }

    public static <E> E getServiceByRelatedClass(Class<E> interf,
            Class<?> related) {
        if (!classRelatedServices.containsKey(interf)) {
            loadClasses(interf);
        }
        synchronized (classRelatedServices) {
            return (E) classRelatedServices.get(interf).get(related);
        }
    }

//    private static <E> boolean isRelatedWithClass(Class<?> related, E obj) {
//        if (!obj.getClass().isAnnotationPresent(ClassBasedService.class)) {
//            return false;
//        }
//        for (Class<?> clazz : obj.getClass()
//                .getAnnotation(ClassBasedService.class).value()) {
//            if (clazz == related) {
//                return true;
//            }
//        }
//        return false;
//    }
    private static <E> void loadClasses(Class<E> interf) {
        synchronized (classRelatedServices) {
            Map<Class, Object> map = new HashMap<>();
            classRelatedServices.put(interf, map);
            var loader = ServiceLoader.load(interf);
            for (E obj : loader) {
                if (obj.getClass().isAnnotationPresent(ClassBasedService.class)) {
                    for (Class<?> clazz : obj.getClass()
                            .getAnnotation(ClassBasedService.class).value()) {
                        map.put(clazz, obj);
                    }
                }
            }

        }
    }

}
