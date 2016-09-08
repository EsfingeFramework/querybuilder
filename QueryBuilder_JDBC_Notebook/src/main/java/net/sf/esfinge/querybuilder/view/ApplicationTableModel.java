package net.sf.esfinge.querybuilder.view;

import javax.swing.table.AbstractTableModel;

public class ApplicationTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 4902931223965279549L;

	private String[] columnNames = new String[] { "ID", "NAME", "SURNAME",
			"BIRTH DATE", "E-MAIL", "PHONE", "CELL", "STREET", "NUMBER",
			"COMPLEMENT", "ZIPCODE", "CITY", "STATE","TYPE" };

	private Object[][] data;

	public Object[][] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	public int getColumnCount() {

		return columnNames.length;
	}

	public int getRowCount() {

		return getData().length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public boolean isCellEditable(int row, int col) {

		return false;

	}

}
