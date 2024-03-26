package net.sf.esfinge.querybuilder.jdbc;

import net.sf.esfinge.querybuilder.jdbc.testresources.Person;
import net.sf.esfinge.querybuilder.utils.Query;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestQuery {

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void selectAllQuery() {
        var query = new Query();
        var person = new Person();
        var generatedQuery = "";
        query.setObj(person);
        try {
            query.setCommandType(CommandType.SELECT_ALL);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id");
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void selectQuerySingle() {

        var query = new Query();
        var generatedQuery = "";
        var person = new Person();
        person.setId(1);
        query.setObj(person);
        try {
            query.setCommandType(CommandType.SELECT_SINGLE);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.id = 1 and person.address_id = address.id");
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void selectQueryExists() {
        var query = new Query();
        var generatedQuery = "";
        var person = new Person();
        person.setId(1);
        query.setObj(person);
        try {
            query.setCommandType(CommandType.SELECT_EXISTS);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.id = 1 and person.address_id = address.id");
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void selectQueryByExample() {
        var query = new Query();
        var generatedQuery = "";
        var person = new Person();
        person.setId(1);
        query.setObj(person);
        try {
            query.setCommandType(CommandType.SELECT_BY_EXAMPLE);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.id = 1 and address.id = 1 and person.address_id = address.id");
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void insertQuery() {
        var query = new Query();
        var generatedQuery = "";
        var person = new Person();
        person.setId(1);
        person.setName("Rafael");
        person.setLastName("Lira");
        person.setAge(25);
        query.setObj(person);
        try {
            query.setCommandType(CommandType.INSERT);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "insert into person (id,name,lastname,age,address_id) values (1,'Rafael','Lira',25,null)");
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void updateQuery() {
        var query = new Query();
        var generatedQuery = "";
        var person = new Person();
        person.setId(1);
        person.setName("Rafael");
        person.setLastName("Lira");
        person.setAge(25);
        query.setObj(person);

        try {
            query.setCommandType(CommandType.UPDATE);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "update person set name = 'Rafael', lastName = 'Lira', age = 25, ADDRESS_ID = null where id = 1");
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void deleteQuery() {
        var query = new Query();
        var generatedQuery = "";
        var person = new Person();
        person.setId(1);
        query.setObj(person);
        try {
            query.setCommandType(CommandType.DELETE);
            generatedQuery = query.buildCommand();
        } catch (Exception err) {
            err.printStackTrace();
        }
        assertEquals(generatedQuery, "delete from person where id = 1");
    }

}
