package esfinge.querybuilder.jpa1_local;

import esfinge.querybuilder.core.annotation.QueryExecutorType;
import esfinge.querybuilder.core.annotation.ServicePriority;
import esfinge.querybuilder.jpa1.JPARepository;

@ServicePriority(1)
@QueryExecutorType("JPA1")
public class JPALocalRepository<E> extends JPARepository<E> {

    @Override
    public E save(E obj) {
        em.getTransaction().begin();
        var saved = super.save(obj);
        em.getTransaction().commit();
        return saved;
    }

    @Override
    public void delete(Object id) {
        em.getTransaction().begin();
        super.delete(id);
        em.getTransaction().commit();
    }

}
