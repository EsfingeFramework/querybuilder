package net.sf.esfinge.querybuilder.utils;

import java.util.ArrayList;
import net.sf.esfinge.querybuilder.annotations.Column;
import net.sf.esfinge.querybuilder.annotations.ID;
import net.sf.esfinge.querybuilder.annotations.JoinColumn;
import net.sf.esfinge.querybuilder.annotations.Table;
import net.sf.esfinge.querybuilder.finder.FinderManager;
import net.sf.esfinge.querybuilder.finder.XmlEntityFinder;

public class SQLUtils {

    private ArrayList<String> listOfChildEntities = null;
    private ArrayList<String> listOfFieldsEntities = null;
    private ArrayList<String> listOfJoinExpressions = null;
    private ArrayList<String> listOfColumnsToInsertComand = null;
    private ArrayList<String> listOfColumnsToUpdateComand = null;
    private ArrayList<String> listOfJoinColumns = null;
    private String mainEntity = "*EMPTY*";
    private String primaryKeyOfMainEntity = "*EMPTY*";

    public ArrayList<String> getListOfJoinColumns() {
        return listOfJoinColumns;
    }

    public void setListOfJoinColumns(ArrayList<String> listOfJoinColumns) {
        this.listOfJoinColumns = listOfJoinColumns;
    }

    public String getPrimaryKeyOfMainEntity() {
        return primaryKeyOfMainEntity;
    }

    private void setPrimaryKeyOfMainEntity(String primaryKeyOfMainEntity) {
        this.primaryKeyOfMainEntity = primaryKeyOfMainEntity;
    }

    public ArrayList<String> getListOfColumnsToInsertComand() {
        return listOfColumnsToInsertComand;
    }

    public boolean isJoinColumn(String columnName) {

        for (var str : listOfJoinColumns) {

            if (str.equalsIgnoreCase(columnName)) {
                return true;
            }

        }
        return false;
    }

    public String getColumnsToInsertComand() {

        var stB = new StringBuilder();
        stB.append("(");
        var firstIteration = true;
        for (var str : listOfColumnsToInsertComand) {

            if (firstIteration) {
                firstIteration = false;
            } else {
                stB.append(",");
            }

            if (str.contains(".")) {

                str.indexOf(".");
                str = (str.substring((str.indexOf(".") + 1)));

            }

            stB.append(str);
        }
        stB.append(")");
        return stB.toString().toLowerCase();
    }

    public ArrayList<String> getListOfColumnsToUpdateComand() {
        return listOfColumnsToUpdateComand;
    }

    public String getMainEntity() {
        return mainEntity;
    }

    private void setMainEntity(String mainEntity) {
        this.mainEntity = mainEntity;
    }

    public SQLUtils(String entity) {

        listOfChildEntities = new ArrayList<>();
        listOfJoinExpressions = new ArrayList<>();
        listOfFieldsEntities = new ArrayList<>();
        listOfColumnsToInsertComand = new ArrayList<>();
        listOfColumnsToUpdateComand = new ArrayList<>();
        listOfJoinColumns = new ArrayList<>();

        loadAndAnalyzeEntities(entity);

    }

    public ArrayList<String> getListOfFieldsEntities() {
        return listOfFieldsEntities;
    }

    public String getFieldsEntities() {

        var stB = new StringBuilder();
        var i = 0;
        for (var str : listOfFieldsEntities) {

            if (i > 0) {

                stB.append(", ");

            }

            stB.append(str);

            i++;

        }

        return stB.toString();

    }

    public boolean haveJoinColumn() {
        return (listOfJoinExpressions.size() > 0) ? true : false;
    }

    public String getChildEntities() {

        var stB = new StringBuilder();
        var i = 0;
        for (var str : listOfChildEntities) {

            if (i > 0) {

                stB.append(", ");

            }

            stB.append(str);

            i++;

        }

        return stB.toString();

    }

    public String getJoinExpressions() {

        var stB = new StringBuilder();
        var i = 0;
        for (var str : listOfJoinExpressions) {

            if (i > 0) {

                stB.append(" and ");

            }

            stB.append(str);

            i++;

        }

        return stB.toString().toLowerCase();

    }

    private void loadAndAnalyzeEntities(String entity) {

        String tableName = null;

        var finderManager = new FinderManager(new XmlEntityFinder());
        String resourceToFind = null;

        resourceToFind = finderManager.find(entity);

        Class<?> clasz = null;

        try {

            clasz = Class.forName(resourceToFind).newInstance().getClass();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (clasz.isAnnotationPresent(Table.class)) {
            var table = clasz.getAnnotation(Table.class);
            tableName = table.name();

        } else {

            tableName = clasz.getSimpleName();
        }

        if (mainEntity.equals("*EMPTY*")) {
            setMainEntity(tableName);
        }

        listOfChildEntities.add(tableName);

        String columnName = null;
        String joinExpression = null;

        var fields = clasz.getDeclaredFields();
        for (var f : fields) {

            if (f.isAnnotationPresent(JoinColumn.class)) {

                var joinColumn = f.getAnnotation(JoinColumn.class);
                var table = f.getAnnotation(Table.class);

                joinExpression = tableName + "." + joinColumn.name() + " = "
                        + f.getType().getSimpleName().toLowerCase() + "."
                        + joinColumn.referencedColumnName();

                listOfJoinExpressions.add(joinExpression);

                listOfJoinColumns.add(joinColumn.name());

                listOfColumnsToInsertComand.add(table.name() + "."
                        + joinColumn.name());

                listOfColumnsToUpdateComand.add(joinColumn.name());

                loadAndAnalyzeEntities(f.getType().getSimpleName());
            } else {

                var isAutoIncrement = false;

                if (f.isAnnotationPresent(Column.class)) {

                    var column = f.getAnnotation(Column.class);
                    columnName = column.name();

                    isAutoIncrement = column.autoIncrement();

                } else {

                    columnName = f.getName();

                }

                if (getPrimaryKeyOfMainEntity().equals("*EMPTY*")) {
                    if (f.isAnnotationPresent(ID.class)) {

                        if (f.isAnnotationPresent(Column.class)) {
                            var column = f.getAnnotation(Column.class);
                            setPrimaryKeyOfMainEntity(column.name());
                        } else {

                            setPrimaryKeyOfMainEntity(f.getName());
                        }

                    }
                }

                if (!isAutoIncrement) {
                    if (getMainEntity().contains(tableName)) {
                        listOfColumnsToInsertComand.add(columnName);

                        if (!f.isAnnotationPresent(ID.class)) {
                            listOfColumnsToUpdateComand.add(columnName);
                        }

                    }
                }

                listOfFieldsEntities.add(tableName + "." + columnName);

            }

        }

    }

}
