package net.sf.esfinge.querybuilder.utils;

public class LineField {

    private String fieldName;
    private Object fieldValue;
    private Class<?> fieldType;

    private boolean isPrimaryKey = false;
    private boolean isAutoIncrementColumn = false;

    public boolean isAutoIncrementColumn() {
        return isAutoIncrementColumn;
    }

    public void setAutoIncrementColumn(boolean isAutoIncrementColumn) {
        this.isAutoIncrementColumn = isAutoIncrementColumn;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

}
