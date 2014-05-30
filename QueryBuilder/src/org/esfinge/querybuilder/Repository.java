package org.esfinge.querybuilder;

import java.util.List;

public interface Repository<E> extends NeedClassConfiguration<E> {
	
	public E save(E obj);
	
	public void delete(Object id);
	
	public List<E> list();
	
	public E getById(Object id);
	
	public List<E> queryByExample(E obj);

}
