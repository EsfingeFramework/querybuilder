package net.sf.esfinge.querybuilder.utils.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;


/*
 * Helper class useful in store all class's fields even the
 * fields from non primitive attributes
 ***/
public class ClassFieldExtractor {

    private ArrayList<Field> ClassFieldList = new ArrayList<>();
    private Class<?> innerClass = null;

    public ClassFieldExtractor(Class<?> clazz) {
        this.innerClass = clazz;
    }

    public void ExtractAllClassFields() {
        var fields = innerClass.getDeclaredFields();
        ClassFieldList.clear();

        for (var f : fields) {
            SearchAllFields(f);
        }
    }

    public ArrayList<Field> getClassFieldList() {
        return ClassFieldList;
    }

    public void setClassFieldList(ArrayList<Field> classFieldList) {
        ClassFieldList = classFieldList;
    }

    public Class<?> getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(Class<?> innerClass) {
        this.innerClass = innerClass;
    }

    public ArrayList<Field> GetAllClassFields() {
        return ClassFieldList;
    }

    private void SearchAllFields(Field f) {
        if (!CheckPrimitiveStringWrapperTypeValue(f.getType())) {
            var fields = f.getType().getDeclaredFields();
            for (var f2 : fields) {
                SearchAllFields(f2);
            }
        } else {
            ClassFieldList.add(f);
        }
    }

    public static boolean CheckPrimitiveStringWrapperTypeValue(Class<?> clazz) {
        return clazz.equals(Boolean.class)
                || clazz.equals(Character.class)
                || clazz.equals(Byte.class)
                || clazz.equals(Short.class)
                || clazz.equals(Integer.class)
                || clazz.equals(Long.class)
                || clazz.equals(Float.class)
                || clazz.equals(Double.class)
                || clazz.equals(Date.class)
                || clazz.equals(boolean.class)
                || clazz.equals(char.class)
                || clazz.equals(byte.class)
                || clazz.equals(short.class)
                || clazz.equals(int.class)
                || clazz.equals(long.class)
                || clazz.equals(float.class)
                || clazz.equals(double.class)
                || clazz.equals(String.class)
                || clazz.equals(void.class);
    }
}
