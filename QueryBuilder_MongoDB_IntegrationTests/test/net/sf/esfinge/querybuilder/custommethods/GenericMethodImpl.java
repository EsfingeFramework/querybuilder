package net.sf.esfinge.querybuilder.custommethods;

public class GenericMethodImpl<E> implements GenericMethodInterface<E> {
	
	private Class<E> clazz;
	
	@Override
	public Class<E> getConfigClass() {
		return clazz;
	}

	@Override
	public void configureClass(Class<E> c) {
		clazz = c;
	}

}
