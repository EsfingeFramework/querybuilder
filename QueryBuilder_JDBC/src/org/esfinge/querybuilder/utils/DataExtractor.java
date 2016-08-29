package org.esfinge.querybuilder.utils;

import java.lang.reflect.Field;

import org.esfinge.querybuilder.annotations.Column;
import org.esfinge.querybuilder.annotations.ID;
import org.esfinge.querybuilder.utils.reflection.ClassFieldExtractor;

public class DataExtractor {

	public static Line extract(Object obj) {

		Line line = new Line();
		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field f : fields) {

			LineField lf = new LineField();
			String columnName = "";

			if (f.isAnnotationPresent(Column.class)) {
				Column column = f.getAnnotation(Column.class);
				columnName = column.name().toUpperCase();
				lf.setAutoIncrementColumn(column.autoIncrement());
			} else {

				if (ClassFieldExtractor.CheckPrimitiveStringWrapperTypeValue(f
						.getType())) {
					columnName = f.getName().toUpperCase();
				} else {
					String className = f.getType().toString().split(" ")[1];
					int index = className.lastIndexOf(".") + 1;
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
			boolean fieldAcces = f.isAccessible();
			f.setAccessible((!fieldAcces ? true : fieldAcces));
			try {
				lf.setFieldValue(f.get(obj));
			} catch (IllegalArgumentException e) {

				lf.setFieldValue(null);

			} catch (IllegalAccessException e) {

				lf.setFieldValue(null);
			}
			f.setAccessible(fieldAcces);
			line.AddLineField(lf);
		}
		return line;
	}

}
