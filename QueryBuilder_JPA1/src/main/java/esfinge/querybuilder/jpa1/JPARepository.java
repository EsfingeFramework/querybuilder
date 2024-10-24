package esfinge.querybuilder.jpa1;

import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core.annotation.QueryExecutorType;
import esfinge.querybuilder.core.exception.InvalidPropertyException;
import esfinge.querybuilder.core.utils.ServiceLocator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Id;

@QueryExecutorType("JPA1")
public class JPARepository<E> implements Repository<E> {

    protected EntityManager em;
    protected Class<E> configuredClass;

    @Override
    public void configureClass(Class<E> clazz) {
        configuredClass = clazz;
    }

    public JPARepository() {
        var emp = ServiceLocator.getServiceImplementation(EntityManagerProvider.class);
        em = emp.getEntityManager();
    }

    @Override
    public E save(E obj) {
        var saved = em.merge(obj);
        return saved;
    }

    @Override
    public void delete(E obj) {
        try {
            Field idField = null;
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    idField = field;
                    break;
                }
            }
            if (idField == null) {
                throw new IllegalStateException("No @Id field found in the entity class " + obj.getClass().getName());
            }
            idField.setAccessible(true);
            Object id = idField.get(obj);
            var e = em.find(configuredClass, id);
            if (e != null) {
                em.remove(e);
            } else {
                throw new IllegalStateException("Entity not found in the context with ID: " + id);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access ID field", e);
        }
    }

    @Override
    public List<E> list() {
        var q = em.createQuery("select o from " + configuredClass.getSimpleName() + " o");
        return q.getResultList();
    }

    @Override
    public E getById(Object id) {
        return em.find(configuredClass, id);
    }

    @Override
    public List<E> queryByExample(E obj) {
        var clazzObj = obj.getClass();
        var query = new StringBuilder("select o from "
                + clazzObj.getSimpleName() + " o where ");
        Map<String, Object> params = new HashMap<>();
        var first = true;
        try {
            for (var m : clazzObj.getMethods()) {
                if (!m.getName().equals("getClass")
                        && JPADAOUtils.isGetterWhichIsNotTransient(m, clazzObj)) {
                    var value = m.invoke(obj);
                    if (value != null && !value.toString().trim().equals("")) {
                        var prop = m.getName().substring(3, 4).toLowerCase()
                                + m.getName().substring(4);
                        params.put(prop, value);
                        if (first) {
                            first = false;
                        } else {
                            query.append(" and");
                        }
                        query.append(" o.").append(prop).append("= :").append(prop);
                    }

                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException e) {
            throw new InvalidPropertyException("Error building query", e);
        }

        var q = em.createQuery(query.toString());
        for (var prop : params.keySet()) {
            q.setParameter(prop, params.get(prop));
        }
        return q.getResultList();
    }
}
