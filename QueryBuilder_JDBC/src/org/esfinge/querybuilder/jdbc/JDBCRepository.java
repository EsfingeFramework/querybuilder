package org.esfinge.querybuilder.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.esfinge.querybuilder.utils.DataBaseChannel;
import org.esfinge.querybuilder.utils.EntityParser;
import org.esfinge.querybuilder.utils.Query;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class JDBCRepository<E> implements Repository<E> {

	private Connection c;
	private Class<E> clazz;
	private DataBaseChannel channel;
	private Object objectForSelectExists;

	public JDBCRepository() {
		findAndOpenConnection();
	}

	private void findAndOpenConnection() {
		DatabaseConnectionProvider dcp = ServiceLocator
				.getServiceImplementation(DatabaseConnectionProvider.class);
		c = dcp.getConnection();
		channel = new DataBaseChannel(c);
	}

	@Override
	public E save(E obj) {

		objectForSelectExists = obj;
		Query query = new Query();

		if (getById(null) == null) {

			query.setCommandType(CommandType.INSERT);

		} else {

			query.setCommandType(CommandType.UPDATE);
		}

		query.setObj(obj);

		try {
			query.buildCommand();

			if (!channel.checkConnection()) {
				findAndOpenConnection();
			}

			channel.executeUpdate(query.buildCommand());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;

	}

	@Override
	public void delete(Object id) {

		Query query = new Query();
		query.setCommandType(CommandType.DELETE);

		try {
			query.setObj(clazz.newInstance());
		} catch (InstantiationException e1) {

			e1.printStackTrace();
		} catch (IllegalAccessException e1) {

			e1.printStackTrace();
		}

		query.setIdValue(id);

		try {
			query.buildCommand();

			if (!channel.checkConnection()) {
				findAndOpenConnection();
			}

			channel.executeUpdate(query.buildCommand());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> list() {

		Query query = new Query();
		EntityParser ep = new EntityParser(clazz);
		query.setCommandType(CommandType.SELECT_ALL);
		List<Object> listaRegistros = new ArrayList<Object>();

		try {
			query.setObj(clazz.newInstance());
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		try {

			if (!channel.checkConnection()) {
				findAndOpenConnection();
			}

			listaRegistros = ep.parseEntity(channel.executeQuery(query
					.buildCommand()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (List<E>) listaRegistros;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getById(Object id) {

		Query query = new Query();
		List<Object> oneRecord = new ArrayList<Object>();
		EntityParser ep = null;

		if (id == null) {

			query.setObj(objectForSelectExists);
			ep = new EntityParser(clazz);
			query.setCommandType(CommandType.SELECT_EXISTS);

		} else {

			try {
				query.setObj(clazz.newInstance());
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			query.setIdValue(id);
			ep = new EntityParser(clazz);

			query.setCommandType(CommandType.SELECT_SINGLE);

		}

		try {

			if (!channel.checkConnection()) {
				findAndOpenConnection();
			}

			oneRecord = ep.parseEntity(channel.executeQuery(query
					.buildCommand()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (oneRecord.size() > 0 ? (E) oneRecord.get(0) : null);
	}

	@Override
	public void configureClass(Class<E> clazz) {

		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> queryByExample(E obj) {

		Query query = new Query();
		EntityParser ep = new EntityParser(obj.getClass());
		query.setCommandType(CommandType.SELECT_BY_EXAMPLE);
		List<Object> listaRegistros = new ArrayList<Object>();

		query.setObj(obj);

		try {

			if (!channel.checkConnection()) {
				findAndOpenConnection();
			}

			listaRegistros = ep.parseEntity(channel.executeQuery(query
					.buildCommand()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (List<E>) listaRegistros;
	}

}
