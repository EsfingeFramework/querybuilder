package net.sf.esfinge.querybuilder.utils;

import net.sf.esfinge.querybuilder.annotations.Column;
import net.sf.esfinge.querybuilder.annotations.ID;
import net.sf.esfinge.querybuilder.utils.reflection.ClassFieldExtractor;

public class DataExtractor {

    public static Line extract(Object obj) {

        var line = new Line();
        var fields = obj.getClass().getDeclaredFields();
        for (var f : fields) {

            var lf = new LineField();
            @SuppressWarnings("UnusedAssignment")
            var columnName = "";

            if (f.isAnnotationPresent(Column.class)) {
                var column = f.getAnnotation(Column.class);
                columnName = column.name().toUpperCase();
                lf.setAutoIncrementColumn(column.autoIncrement());
            } else {

                if (ClassFieldExtractor.CheckPrimitiveStringWrapperTypeValue(f
                        .getType())) {
                    columnName = f.getName().toUpperCase();
                } else {
                    var className = f.getType().toString().split(" ")[1];
                    var index = className.lastIndexOf(".") + 1;
                    if (index > 0) {
                        className = className.substring(index);
                    }
                    columnName = className + ".*";
                }
            }

            lf.setFieldName(columnName);
            if (f.isAnnotationPresent(ID.class)) {
                lf.setPrimaryKey(true);
            }
            var fieldAcces = f.isAccessible();
            f.setAccessible((!fieldAcces ? true : fieldAcces));
            try {
                lf.setFieldValue(f.get(obj));
            } catch (IllegalArgumentException | IllegalAccessException e) {

                lf.setFieldValue(null);

            }
            f.setAccessible(fieldAcces);
            line.AddLineField(lf);
        }
        return line;
    }

}
