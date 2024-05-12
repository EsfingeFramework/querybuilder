package esfinge.querybuilder.jpa1;

import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core.exception.InvalidPropertyException;
import esfinge.querybuilder.core.utils.ServiceLocator;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JPARepository<E> implements Repository<E> {

    protected EntityManager em;
    protected Class<E> clazz;

    public JPARepository() {
        EntityManagerProvider emp = ServiceLocator.getServiceImplementation(EntityManagerProvider.class);
        em = emp.getEntityManager();
    }

    @Override
    public E save(E obj) {
        E saved = em.merge(obj);
        return saved;
    }

    @Override
    public void delete(Object id) {
        E e = em.find(clazz, id);
        em.remove(e);
    }

    @Override
    public List<E> list() {
        Query q = em.createQuery("select o from " + clazz.getSimpleName() + " o");
        return q.getResultList();
    }

    @Override
    public E getById(Object id) {
        return em.find(clazz, id);
    }

    @Override
    public void configureClass(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<E> queryByExample(E obj) {
        Class<?> clazz = obj.getClass();
        StringBuilder query = new StringBuilder("select o from "
                + clazz.getSimpleName() + " o where ");
        Map<String, Object> params = new HashMap<String, Object>();
        boolean first = true;
        try {
            for (Method m : clazz.getMethods()) {
                if (!m.getName().equals("getClass")
                        && JPADAOUtils.isGetterWhichIsNotTransient(m, clazz)) {
                    Object value = m.invoke(obj);
                    if (value != null && !value.toString().trim().equals("")) {
                        String prop = m.getName().substring(3, 4).toLowerCase()
                                + m.getName().substring(4);
                        params.put(prop, value);
                        if (first) {
                            first = false;
                        } else {
                            query.append(" and");
                        }
                        query.append(" o." + prop + "= :" + prop);
                    }

                }
            }
        } catch (Exception e) {
            throw new InvalidPropertyException("Error building query", e);
        }

        Query q = em.createQuery(query.toString().toString());
        for (String prop : params.keySet()) {
            q.setParameter(prop, params.get(prop));
        }
        return q.getResultList();
    }

}
