package org.esfinge.querybuilder;

public interface NeedClassConfiguration<E> {

	public abstract void configureClass(Class<E> clazz);

}