package org.esfinge.querybuilder.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.esfinge.querybuilder.annotations.JoinColumn;

public class ReflectionOperations {
	public static boolean SetFieldValue(Field field, Object objetoToSetValue,
			Object value) throws Exception {
		boolean result = true;
		try {
			boolean fieldActualStatus = field.isAccessible();
			field.setAccessible((!fieldActualStatus ? true : false));
			field.set(objetoToSetValue, value);
			field.setAccessible(fieldActualStatus);
		} catch (Exception err) {
			result = false;
		}
		return result;
	}

	public Object findAttributeInClass(Object obj, String fieldName) {

		Class<? extends Object> cls = obj.getClass();
		Object objectResult = null;
		String newField = null;
		Object joinTable = null;

		Field fieldlist[] = cls.getDeclaredFields();

		for (Field fld : fieldlist) {

			if (fld.getName().equalsIgnoreCase(fieldName)) {

				String method = "get"
						+ fld.getName().substring(0, 1).toUpperCase()
						+ fld.getName().substring(1);
				try {
					Method meth = cls.getMethod(method, null);
					joinTable = meth.invoke(obj, null);
				} catch (Exception e1) {
					objectResult = null;
				}

				if (fld.isAnnotationPresent(JoinColumn.class)) {

					JoinColumn jnc = fld.getAnnotation(JoinColumn.class);
					newField = jnc.referencedColumnName();

				}

				Class<? extends Object> newCls = fld.getType();

				Field newFieldList[] = newCls.getDeclaredFields();

				for (Field newFld : newFieldList) {

					if (newFld.getName().equalsIgnoreCase(newField)) {

						newFld.setAccessible(true);

						try {
							objectResult = newFld.get(joinTable);
						} catch (Exception e) {
							objectResult = null;
						}

						break;

					}

				}

				break;
			}

		}

		return objectResult;

	}

	public static Object findAttributeInClass(Object enclosingObject,
			Class<?> attributeToFind) throws Exception {
		Object ObjectResult = null;
		boolean actualStatus = false;
		try {

			for (Field field : enclosingObject.getClass().getDeclaredFields()) {
				if (field.getType().equals(attributeToFind)) {
					String fieldClass = field.getType().toString().split(" ")[1]
							.toString();
					actualStatus = field.isAccessible();
					field.setAccessible(!actualStatus ? true : actualStatus);
					ObjectResult = field.get(enclosingObject);

					if (ObjectResult == null) {
						ObjectResult = Class.forName(fieldClass).newInstance();
					}

					field.set(enclosingObject, ObjectResult);
					field.setAccessible(actualStatus);
					break;
				}
			}
		} catch (Exception err) {
			ObjectResult = null;
		}

		return ObjectResult;
	}

}