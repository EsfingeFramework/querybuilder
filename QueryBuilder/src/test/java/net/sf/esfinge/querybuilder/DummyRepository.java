package net.sf.esfinge.querybuilder;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.querybuilder.Repository;

public class DummyRepository<E> implements Repository<E>{
	
	public static DummyRepository<?> instance;

	public DummyRepository() {
		super();
		instance = this;
	}

	private String lastMethodCalled;
	private Class configuredClass;
	
	@Override
	public E save(E obj) {
		lastMethodCalled = "save";
		return null;
	}

	@Override
	public void delete(Object id) {
		lastMethodCalled = "delete";
		
	}

	@Override
	public List<E> list() {
		lastMethodCalled = "list";
		return null;
	}

	@Override
	public E getById(Object id) {
		lastMethodCalled = "getById";
		return null;
	}

	@Override
	public void configureClass(Class<E> clazz) {
		configuredClass = clazz;	
	}

	public String getLastMethodCalled() {
		return lastMethodCalled;
	}

	public Class getConfiguredClass() {
		return configuredClass;
	}

	@Override
	public List<E> queryByExample(E obj) {
		lastMethodCalled = "queryByExample";
		return null;
	}

}
