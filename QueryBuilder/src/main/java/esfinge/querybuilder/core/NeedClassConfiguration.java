package esfinge.querybuilder.core;

public interface NeedClassConfiguration<E> {

    void setConfiguredClass(Class<E> clazz);

    Class<E> getConfiguredClass();
}
