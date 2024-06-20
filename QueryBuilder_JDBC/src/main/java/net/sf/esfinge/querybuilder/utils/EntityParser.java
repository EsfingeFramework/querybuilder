package net.sf.esfinge.querybuilder.utils;

import java.util.ArrayList;
import java.util.List;
import net.sf.esfinge.querybuilder.annotations.Column;
import net.sf.esfinge.querybuilder.utils.reflection.ClassFieldExtractor;
import net.sf.esfinge.querybuilder.utils.reflection.ReflectionOperations;

public class EntityParser {

    private Class<?> innerClass;

    public EntityParser(Class<?> innerClass) {
        this.setInnerClass(innerClass);
    }

    public ArrayList<Object> parseEntity(List<Line> tableLines)
            throws ClassNotFoundException, Exception {
        var Result = new ArrayList<>();
        var columnName = "";
        for (var line : tableLines) {
            var object = Class.forName(innerClass.getName()).newInstance();
            var find = false;
            var fieldExtractor = new ClassFieldExtractor(
                    innerClass);
            fieldExtractor.ExtractAllClassFields();

            for (var f : fieldExtractor.GetAllClassFields()) {

                if (f.isAnnotationPresent(Column.class)) {
                    var column = f.getAnnotation(Column.class);
                    columnName = column.name().toUpperCase();
                } else {
                    columnName = f.getName().toUpperCase();
                }

                var value = line.getValueByFieldName(columnName);

                if (value != null) {

                    var childClass = f.getDeclaringClass();
                    var fatherClass = object.getClass();
                    if (childClass.equals(fatherClass)) {
                        ReflectionOperations.SetFieldValue(f, object, value);
                        find = true;
                    } else {

                        Object childObject = null;
                        childObject = ReflectionOperations
                                .findAttributeInClass(object,
                                        f.getDeclaringClass());
                        ReflectionOperations.SetFieldValue(f, childObject,
                                value);
                        find = true;
                    }
                }
                columnName = "";
                value = null;
            }

            if (find) {

                Result.add(object);
            }
        }
        return Result;
    }

    public Class<?> getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(Class<?> innerClass) {
        this.innerClass = innerClass;
    }

}
