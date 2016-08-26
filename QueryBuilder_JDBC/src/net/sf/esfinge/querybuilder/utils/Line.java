package net.sf.esfinge.querybuilder.utils;

import java.util.ArrayList;

public class Line {

	private ArrayList<LineField> fields = new ArrayList<LineField>();

	public void AddLineField(LineField field) {
		fields.add(field);
	}

	public ArrayList<LineField> getAllFields() {
		return this.fields;
	}

	public String findPrimaryKey() {
		for (LineField lf : fields) {
			if (lf.isPrimaryKey()) {
				return lf.getFieldName();
			}
		}
		return null;
	}

	public Object getValueByFieldName(String FieldName) {

		if (fields != null && fields.size() > 0) {

			for (LineField field : fields) {

				if (field.getFieldName().toUpperCase()
						.equals(FieldName.toUpperCase()))

					return field.getFieldValue();
			}
		}
		return null;
	}

}
