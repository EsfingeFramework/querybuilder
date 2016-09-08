package net.sf.esfinge.querybuilder.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.text.MaskFormatter;

public class ApplicationComponents extends JFrame {

	private static final long serialVersionUID = -8086201978339680527L;

	private JLabel lblContactId = new JLabel("ID : ");
	private JLabel lblContactEmail = new JLabel("E-Mail : ");
	private JLabel lblContactPhoneNumber = new JLabel("Phone Number : ");
	private JLabel lblContactCellNumber = new JLabel("Cell Number : ");

	private JLabel lblContactName = new JLabel("Name : ");
	private JLabel lblContactSurName = new JLabel("Sur Name : ");
	private JLabel lblContactBirthDate = new JLabel("Birth Date : ");

	private JLabel lblContactStreet = new JLabel("Street : ");
	private JLabel lblContactStreetNumber = new JLabel("Street Number : ");
	private JLabel lblContactStreetComplement = new JLabel("Street Complement : ");
	private JLabel lblContactCity = new JLabel("City : ");
	private JLabel lblContactState = new JLabel("State : ");
	private JLabel lblContactType = new JLabel("Type of Contact : ");
	private JLabel lblContactZipcode = new JLabel("Zipcode : ");

	private JTextField txtContactId = new JTextField();
	private JTextField txtContactEmail = new JTextField();
	private JFormattedTextField txtContactPhoneNumber = new JFormattedTextField();
	private JFormattedTextField txtContactCellNumber = new JFormattedTextField();

	private JTextField txtContactName = new JTextField();
	private JTextField txtContactSurName = new JTextField();
	private JFormattedTextField txtContactBirthDate = new JFormattedTextField();

	private JTextField txtContactStreet = new JTextField();
	private JFormattedTextField txtContactStreetNumber = new JFormattedTextField();
	private JTextField txtContactStreetComplement = new JTextField();
	private JTextField txtContactCity = new JTextField();
	private JTextField txtContactState = new JTextField();
	private Object[] listContactTypes = { " ", "COWORKER", "FAMILIAR", "FRIEND", };

	private JComboBox cmbContactType = new JComboBox(listContactTypes);
	private JFormattedTextField txtContactZipcode = new JFormattedTextField();

	private JPanel panelLabels = new JPanel();
	private JPanel panelTextFields = new JPanel();
	private JPanel panelOperations = new JPanel();
	private JPanel panelJTable = new JPanel();
	private JTable tableView = new JTable();
	private JScrollPane scrollpane = new JScrollPane(tableView);

	private JButton btnNewContact = new JButton("New", new ImageIcon(getClass().getClassLoader().getResource("./images/new.png").getFile()));
	private JButton btnSaveContact = new JButton("Save", new ImageIcon(getClass().getClassLoader().getResource("./images/save.png").getFile()));
	private JButton btnCancelContact = new JButton("Cancel", new ImageIcon(getClass().getClassLoader().getResource("images/cancel.png").getFile()));
	private JButton btnDeleteContact = new JButton("Delete", new ImageIcon(getClass().getClassLoader().getResource("./images/delete.png").getFile()));
	private JButton btnSearch = new JButton("Search", new ImageIcon(getClass().getClassLoader().getResource("./images/search.png").getFile()));
	private JButton btnCloseApplication = new JButton("Close", new ImageIcon(getClass().getClassLoader().getResource("./images/exit.png").getFile()));
	private JButton btnModifyContact = new JButton("Modify", new ImageIcon(getClass().getClassLoader().getResource("./images/modify.png").getFile()));
	private JButton btnGoSearch = new JButton("Go Search", new ImageIcon(getClass().getClassLoader().getResource("./images/ok.png").getFile()));
	private JButton btnSearchFriends = new JButton("Friends", new ImageIcon(getClass().getClassLoader().getResource("./images/cell.png").getFile()));

	public JButton getBtnSearchFriends() {
		return btnSearchFriends;
	}

	public JLabel getLblContactType() {
		return lblContactType;
	}

	public JButton getBtnCloseApplication() {
		return btnCloseApplication;
	}

	public JScrollPane getScrollpane() {
		return scrollpane;
	}

	public JTable getTableView() {
		return tableView;
	}

	public JButton getBtnModifyContact() {
		return btnModifyContact;
	}

	public JButton getBtnGoSearch() {
		return btnGoSearch;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public JComboBox getCmbContactType() {
		return cmbContactType;
	}

	public JPanel getPanelLabels(int x, int y, int width, int heigth) {
		setPanelLabels(x, y, width, heigth);
		return panelLabels;
	}

	public JPanel getPanelTextFields(int x, int y, int width, int heigth) {
		setPanelTextFields(x, y, width, heigth);
		return panelTextFields;
	}

	public JPanel getPanelOperations(int x, int y, int width, int heigth) {

		setPanelOperations(x, y, width, heigth);
		return panelOperations;
	}

	public JPanel getPanelJTable(int x, int y, int width, int heigth) {

		setPanelJTable(x, y, width, heigth);
		return panelJTable;
	}

	public JButton getBtnNewContact() {
		return btnNewContact;
	}

	public JButton getBtnSaveContact() {
		return btnSaveContact;
	}

	public JButton getBtnCancelContact() {
		return btnCancelContact;
	}

	public JButton getBtnDeleteContact() {
		return btnDeleteContact;
	}

	public ApplicationComponents() {

		setAlignmentLabels();

	}

	public JLabel getLblContactId() {
		return lblContactId;
	}

	public JLabel getLblContactEmail() {
		return lblContactEmail;
	}

	public JLabel getLblContactPhoneNumber() {
		return lblContactPhoneNumber;
	}

	public JLabel getLblContactCellNumber() {
		return lblContactCellNumber;
	}

	public JLabel getLblContactName() {
		return lblContactName;
	}

	public JLabel getLblContactSurName() {
		return lblContactSurName;
	}

	public JLabel getLblContactBirthDate() {
		return lblContactBirthDate;
	}

	public JLabel getLblContactStreet() {
		return lblContactStreet;
	}

	public JLabel getLblContactStreetNumber() {
		return lblContactStreetNumber;
	}

	public JLabel getLblContactStreetComplement() {
		return lblContactStreetComplement;
	}

	public JLabel getLblContactCity() {
		return lblContactCity;
	}

	public JLabel getLblContactState() {
		return lblContactState;
	}

	public JLabel getLblContactZipcode() {
		return lblContactZipcode;
	}

	public JTextField getTxtContactId() {
		return txtContactId;
	}

	public JTextField getTxtContactEmail() {
		return txtContactEmail;
	}

	public JFormattedTextField getTxtContactPhoneNumber() {
		return txtContactPhoneNumber;
	}

	public JFormattedTextField getTxtContactCellNumber() {
		return txtContactCellNumber;
	}

	public JTextField getTxtContactName() {
		return txtContactName;
	}

	public JTextField getTxtContactSurName() {
		return txtContactSurName;
	}

	public JFormattedTextField getTxtContactBirthDate() {
		return txtContactBirthDate;
	}

	public JTextField getTxtContactStreet() {
		return txtContactStreet;
	}

	public JFormattedTextField getTxtContactStreetNumber() {
		return txtContactStreetNumber;
	}

	public JTextField getTxtContactStreetComplement() {
		return txtContactStreetComplement;
	}

	public JTextField getTxtContactCity() {
		return txtContactCity;
	}

	public JTextField getTxtContactState() {
		return txtContactState;
	}

	public JFormattedTextField getTxtContactZipcode() {
		return txtContactZipcode;
	}

	public void setAlignmentLabels() {
		lblContactId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactPhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactCellNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactSurName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactBirthDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactStreet.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactStreetNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactStreetComplement.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactCity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactZipcode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactType.setHorizontalAlignment(SwingConstants.RIGHT);

	}

	public void setPanelOperations(int x, int y, int width, int heigth) {

		panelOperations.setBounds(x, y, width, heigth);
		panelOperations.setLayout(new GridLayout(0, 9, 2, 2));
		panelOperations.add(getBtnNewContact());
		panelOperations.add(getBtnSaveContact());
		panelOperations.add(getBtnModifyContact());
		panelOperations.add(getBtnSearch());
		panelOperations.add(getBtnGoSearch());
		panelOperations.add(getBtnSearchFriends());
		panelOperations.add(getBtnCancelContact());
		panelOperations.add(getBtnDeleteContact());
		panelOperations.add(getBtnCloseApplication());

	}

	public void setPanelJTable(int x, int y, int width, int heigth) {

		panelJTable.setBounds(x, y, width, heigth);
		panelJTable.setLayout(new GridLayout(0, 1, 5, 5));
		panelJTable.add(getScrollpane());

	}

	public void setPanelLabels(int x, int y, int width, int heigth) {

		panelLabels.setBounds(x, y, width, heigth);
		panelLabels.setLayout(new GridLayout(0, 1, 5, 5));
		panelLabels.add(getLblContactId());
		panelLabels.add(getLblContactName());
		panelLabels.add(getLblContactSurName());
		panelLabels.add(getLblContactBirthDate());
		panelLabels.add(getLblContactEmail());
		panelLabels.add(getLblContactPhoneNumber());
		panelLabels.add(getLblContactCellNumber());
		panelLabels.add(getLblContactStreet());
		panelLabels.add(getLblContactStreetNumber());
		panelLabels.add(getLblContactStreetComplement());
		panelLabels.add(getLblContactZipcode());
		panelLabels.add(getLblContactCity());
		panelLabels.add(getLblContactState());
		panelLabels.add(getLblContactType());

	}

	public void enabledFields() {

		txtContactEmail.setEnabled(true);
		txtContactPhoneNumber.setEnabled(true);
		txtContactCellNumber.setEnabled(true);
		txtContactName.setEnabled(true);
		txtContactSurName.setEnabled(true);
		txtContactBirthDate.setEnabled(true);
		txtContactStreet.setEnabled(true);
		txtContactStreetNumber.setEnabled(true);
		txtContactStreetComplement.setEnabled(true);
		txtContactCity.setEnabled(true);
		txtContactState.setEnabled(true);
		txtContactZipcode.setEnabled(true);
		cmbContactType.setEnabled(true);

	}

	public void disabledFields() {

		txtContactId.setEnabled(false);
		txtContactEmail.setEnabled(false);
		txtContactPhoneNumber.setEnabled(false);
		txtContactCellNumber.setEnabled(false);
		txtContactName.setEnabled(false);
		txtContactSurName.setEnabled(false);
		txtContactBirthDate.setEnabled(false);
		txtContactStreet.setEnabled(false);
		txtContactStreetNumber.setEnabled(false);
		txtContactStreetComplement.setEnabled(false);
		txtContactCity.setEnabled(false);
		txtContactState.setEnabled(false);
		txtContactZipcode.setEnabled(false);
		cmbContactType.setEnabled(false);

	}

	public void cleanFields() {

		txtContactId.setText("");
		txtContactEmail.setText("");
		txtContactPhoneNumber.setText("");
		txtContactCellNumber.setText("");
		txtContactName.setText("");
		txtContactSurName.setText("");
		txtContactBirthDate.setText("");
		txtContactStreet.setText("");
		txtContactStreetNumber.setText("");
		txtContactStreetComplement.setText("");
		txtContactCity.setText("");
		txtContactState.setText("");
		txtContactZipcode.setText("");
		cmbContactType.setSelectedIndex(0);

	}

	public void setOriginalLabelsColor() {

		lblContactId.setForeground(Color.black);
		lblContactName.setForeground(Color.black);
		lblContactSurName.setForeground(Color.black);
		lblContactBirthDate.setForeground(Color.black);
		lblContactPhoneNumber.setForeground(Color.black);
		lblContactEmail.setForeground(Color.black);
		lblContactPhoneNumber.setForeground(Color.black);
		lblContactCity.setForeground(Color.black);
		lblContactState.setForeground(Color.black);
		lblContactStreetNumber.setForeground(Color.black);
		lblContactType.setForeground(Color.black);

	}

	public void enabledTableView(boolean enabled) {

		if (enabled) {

			getTableView().setBackground(Color.white);
		} else {
			getTableView().setBackground(Color.gray);
		}

		getTableView().setEnabled(enabled);

	}

	public void setPanelTextFields(int x, int y, int width, int heigth) {

		MaskFormatter maskBirthDate = null;
		MaskFormatter maskPhoneNumber = null;
		MaskFormatter maskCellNumber = null;
		MaskFormatter maskZipcode = null;

		try {
			maskBirthDate = new MaskFormatter("##/##/####");
			maskPhoneNumber = new MaskFormatter("(###)####-####");
			maskCellNumber = new MaskFormatter("(###)####-####");
			maskZipcode = new MaskFormatter("#####-###");

		} catch (ParseException e) {

		}

		maskBirthDate.install(getTxtContactBirthDate());
		maskPhoneNumber.install(getTxtContactPhoneNumber());
		maskCellNumber.install(getTxtContactCellNumber());
		maskZipcode.install(getTxtContactZipcode());

		panelTextFields.setBounds(x, y, width, heigth);
		panelTextFields.setLayout(new GridLayout(0, 1, 5, 5));
		panelTextFields.add(getTxtContactId());
		panelTextFields.add(getTxtContactName());
		panelTextFields.add(getTxtContactSurName());
		panelTextFields.add(getTxtContactBirthDate());
		panelTextFields.add(getTxtContactEmail());
		panelTextFields.add(getTxtContactPhoneNumber());
		panelTextFields.add(getTxtContactCellNumber());
		panelTextFields.add(getTxtContactStreet());
		panelTextFields.add(getTxtContactStreetNumber());
		panelTextFields.add(getTxtContactStreetComplement());
		panelTextFields.add(getTxtContactZipcode());
		panelTextFields.add(getTxtContactCity());
		panelTextFields.add(getTxtContactState());
		panelTextFields.add(getCmbContactType());

	}

}
