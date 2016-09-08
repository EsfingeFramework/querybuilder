package net.sf.esfinge.querybuilder.view;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class ApplicationView extends ApplicationComponents {

	private static final long serialVersionUID = -7758373077345617890L;
	
	private ApplicationTableModel atm = new ApplicationTableModel();
	
	public ApplicationTableModel getAtm() {
		return atm;
	}

	public ApplicationView() {

		this.setTitle("Application - Notebook");
		this.setSize(1050, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		this.getContentPane().add(getPanelOperations(10, 10, 1020, 23));
		this.getContentPane().add(getPanelLabels(10, 40, 120, 360));
		this.getContentPane().add(getPanelTextFields(135, 40, 895, 360));
		this.getContentPane().add(getPanelJTable(10, 410, 1020, 250));

	}

	public void setTableViewModel(Object data[][]) {
		this.getAtm().setData(data);
		this.getTableView().setModel(this.getAtm());
		this.getTableView().setAutoCreateRowSorter(true);
		this.getTableView().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	public void displayMessage(String title, String message, int typeMessage) {

		switch (typeMessage) {
		case 0:
			typeMessage = JOptionPane.INFORMATION_MESSAGE;
			break;

		case 1:
			typeMessage = JOptionPane.ERROR_MESSAGE;
			break;

		default:
			typeMessage = JOptionPane.INFORMATION_MESSAGE;
			break;
		}

		JOptionPane.showMessageDialog(null, message, title, typeMessage);

	}

	public boolean confirmMessage(String title, String message) {

		int answer = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);

		if (answer == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}

	}

}
