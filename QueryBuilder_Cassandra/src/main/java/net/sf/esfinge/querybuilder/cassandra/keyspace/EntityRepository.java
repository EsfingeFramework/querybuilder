package net.sf.esfinge.querybuilder.cassandra.keyspace;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// TODO: IMPLEMENT METHODS FOR CREATION AND DELETION OF TABLES AND INSERTION OF DATA

public class EntityRepository {

    private static final String TABLE_NAME = "books";

    private static final String TABLE_NAME_BY_TITLE = TABLE_NAME + "ByTitle";

    private Session session;

    public EntityRepository(Session session) {
        this.session = session;
    }

    /**
     * Creates the books table.
     */
    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("title text,")
                .append("author text,")
                .append("subject text);");

        final String query = sb.toString();
        System.out.println(query);

        session.execute(query);
    }

    /*/**
     * Creates the books table.
     */
   /* public void createTableBooksByTitle() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME_BY_TITLE).append("(").append("id uuid, ").append("title text,").append("PRIMARY KEY (title, id));");

        final String query = sb.toString();
        session.execute(query);
    }*/

    /**
     * Alters the table books and adds an extra column.
     */
    /*public void alterTablebooks(String columnName, String columnType) {
        StringBuilder sb = new StringBuilder("ALTER TABLE ").append(TABLE_NAME).append(" ADD ").append(columnName).append(" ").append(columnType).append(";");

        final String query = sb.toString();
        session.execute(query);
    }*/

    /**
     * Insert a row in the table books. 
     * 
     * @param book
     */
    /*public void insertbook(E obj) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(TABLE_NAME).append("(id, title, author, subject) ").append("VALUES (").append(book.getId()).append(", '").append(book.getTitle()).append("', '").append(book.getAuthor()).append("', '")
                .append(book.getSubject()).append("');");

        final String query = sb.toString();
        System.out.println(query);
        session.execute(query);
    }*/

    /**
     * Insert a row in the table booksByTitle.
     * @param book
     */
    /*public void insertbookByTitle(Book book) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(TABLE_NAME_BY_TITLE).append("(id, title) ").append("VALUES (").append(book.getId()).append(", '").append(book.getTitle()).append("');");

        final String query = sb.toString();
        session.execute(query);
    }*/

    /**
     * Insert a book into two identical tables using a batch query.
     * 
     * @param book
     */
    /*public void insertBookBatch(Book book) {
        StringBuilder sb = new StringBuilder("BEGIN BATCH ").append("INSERT INTO ").append(TABLE_NAME).append("(id, title, author, subject) ").append("VALUES (").append(book.getId()).append(", '").append(book.getTitle()).append("', '").append(book.getAuthor())
                .append("', '").append(book.getSubject()).append("');").append("INSERT INTO ").append(TABLE_NAME_BY_TITLE).append("(id, title) ").append("VALUES (").append(book.getId()).append(", '").append(book.getTitle()).append("');")
                .append("APPLY BATCH;");

        final String query = sb.toString();
        session.execute(query);
    }*/

    /**
     * Select book by id.
     * 
     * @return
     */
    /*public Book selectByTitle(String title) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_BY_TITLE).append(" WHERE title = '").append(title).append("';");

        final String query = sb.toString();

        ResultSet rs = session.execute(query);

        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book s = new Book(r.getUUID("id"), r.getString("title"), null, null);
            books.add(s);
        }

        return books.get(0);
    }*/

    /**
     * Select all books from books
     * 
     * @return
     */
   public <E> List<E> selectAll(Class<E> clazz, String keyspaceName) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ")
                .append(keyspaceName)
                .append(".")
                .append(clazz.getSimpleName());

        final String query = sb.toString();

       System.out.println(query);
        ResultSet rs = session.execute(query);

        List<E> objects = new ArrayList<E>();

        Method methods[] = clazz.getMethods();

        for (Row r : rs) {
            //Book book = new Book(r.getUUID("id"), r.getString("title"), r.getString("author"), r.getString("subject"));
            //objects.add(obj);
            /*Constructor<E> constructor = null;
            try {
                constructor = clazz.getConstructor();
                Object obj = constructor.newInstance();


            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/
           System.out.println(r.getString("name"));
        }
        //return objects;
       return null;
    }

    /**
     * Select all books from booksByTitle
     * @return
     */
    /*public List<Book> selectAllBookByTitle() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_BY_TITLE);

        final String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book book = new Book(r.getUUID("id"), r.getString("title"), null, null);
            books.add(book);
        }
        return books;
    }*/

    /**
     * Delete a book by title.
     */
   /* public void deletebookByTitle(String title) {
        StringBuilder sb = new StringBuilder("DELETE FROM ").append(TABLE_NAME_BY_TITLE).append(" WHERE title = '").append(title).append("';");

        final String query = sb.toString();
        session.execute(query);
    }*/

    /**
     * Delete table.
     * 
     * @param tableName the name of the table to delete.
     */
    /*public void deleteTable(String tableName) {
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ").append(tableName);

        final String query = sb.toString();
        session.execute(query);
    }*/
}
