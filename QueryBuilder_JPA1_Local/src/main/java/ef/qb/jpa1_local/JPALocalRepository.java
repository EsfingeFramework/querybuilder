package ef.qb.jpa1_local;

import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.annotation.ServicePriority;
import ef.qb.jpa1.JPARepository;

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
    public void delete(E obj) {
        em.getTransaction().begin();
        super.delete(obj);
        em.getTransaction().commit();
    }

}
