package org.esfinge.querybuilder.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.esfinge.querybuilder.annotations.Column;
import org.esfinge.querybuilder.utils.reflection.ClassFieldExtractor;
import org.esfinge.querybuilder.utils.reflection.ReflectionOperations;

public class EntityParser {

	private Class<?> innerClass;

	public EntityParser(Class<?> innerClass) {
		this.setInnerClass(innerClass);
	}

	public ArrayList<Object> parseEntity(List<Line> tableLines)
			throws ClassNotFoundException, Exception {
		ArrayList<Object> Result = new ArrayList<Object>();
		String columnName = "";

		for (Line line : tableLines) {
			Object object = Class.forName(innerClass.getName()).newInstance();
			boolean find = false;

			ClassFieldExtractor fieldExtractor = new ClassFieldExtractor(
					innerClass);
			fieldExtractor.ExtractAllClassFields();

			for (Field f : fieldExtractor.GetAllClassFields()) {

				if (f.isAnnotationPresent(Column.class)) {
					Column column = f.getAnnotation(Column.class);
					columnName = column.name().toUpperCase();
				} else {
					columnName = f.getName().toUpperCase();
				}

				Object value = line.getValueByFieldName(columnName);

				if (value != null) {

					Class<?> childClass = f.getDeclaringClass();
					Class<?> fatherClass = object.getClass();
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
