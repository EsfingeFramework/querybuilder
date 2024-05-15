package esfinge.querybuilder.jpa1;

import esfinge.querybuilder.core.annotation.ServicePriority;

@ServicePriority(1)
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
