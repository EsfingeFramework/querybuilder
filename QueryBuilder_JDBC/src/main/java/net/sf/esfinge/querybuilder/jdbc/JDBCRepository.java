package net.sf.esfinge.querybuilder.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.utils.DataBaseChannel;
import net.sf.esfinge.querybuilder.utils.EntityParser;
import net.sf.esfinge.querybuilder.utils.Query;
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
        var dcp = ServiceLocator.getServiceImplementation(DatabaseConnectionProvider.class);
        c = dcp.getConnection();
        channel = new DataBaseChannel(c);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public E save(E obj) {
        objectForSelectExists = obj;
        var query = new Query();
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
    @SuppressWarnings("CallToPrintStackTrace")
    public void delete(Object id) {
        var query = new Query();
        query.setCommandType(CommandType.DELETE);
        try {
            query.setObj(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e1) {
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

    @SuppressWarnings({"unchecked", "CallToPrintStackTrace"})
    @Override
    public List<E> list() {

        var query = new Query();
        var ep = new EntityParser(clazz);
        query.setCommandType(CommandType.SELECT_ALL);
        List<Object> listaRegistros = new ArrayList<>();

        try {
            query.setObj(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        try {
            if (!channel.checkConnection()) {
                findAndOpenConnection();
            }
            listaRegistros = ep.parseEntity(channel.executeQuery(query.buildCommand()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (List<E>) listaRegistros;
    }

    @SuppressWarnings({"unchecked", "CallToPrintStackTrace"})
    @Override
    public E getById(Object id) {
        var query = new Query();
        List<Object> oneRecord = new ArrayList<>();
        @SuppressWarnings("UnusedAssignment")
        EntityParser ep = null;
        if (id == null) {
            query.setObj(objectForSelectExists);
            ep = new EntityParser(clazz);
            query.setCommandType(CommandType.SELECT_EXISTS);
        } else {
            try {
                query.setObj(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e1) {
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
            oneRecord = ep.parseEntity(channel.executeQuery(query.buildCommand()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (!oneRecord.isEmpty() ? (E) oneRecord.get(0) : null);
    }

    @Override
    public void configureClass(Class<E> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings({"unchecked", "CallToPrintStackTrace"})
    @Override
    public List<E> queryByExample(E obj) {
        var query = new Query();
        var ep = new EntityParser(obj.getClass());
        query.setCommandType(CommandType.SELECT_BY_EXAMPLE);
        List<Object> listaRegistros = new ArrayList<>();
        query.setObj(obj);
        try {
            if (!channel.checkConnection()) {
                findAndOpenConnection();
            }
            listaRegistros = ep.parseEntity(channel.executeQuery(query.buildCommand()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (List<E>) listaRegistros;
    }
}
