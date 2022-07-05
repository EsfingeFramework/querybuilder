package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.Repository;

import java.util.List;

public class CassandraRepository<E> implements Repository<E> {

    @Override
    public E save(E e) {
        return null;
    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public List<E> list() {
        return null;
    }

    @Override
    public E getById(Object o) {
        return null;
    }

    @Override
    public List<E> queryByExample(E e) {
        return null;
    }

    @Override
    public void configureClass(Class<E> aClass) {

    }
}
