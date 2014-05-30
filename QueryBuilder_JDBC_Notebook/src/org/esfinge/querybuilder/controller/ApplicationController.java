package org.esfinge.querybuilder.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.model.Address;
import org.esfinge.querybuilder.model.Contact;
import org.esfinge.querybuilder.model.Person;
import org.esfinge.querybuilder.model.persistence.AddressOperations;
import org.esfinge.querybuilder.model.persistence.ContactOperations;
import org.esfinge.querybuilder.model.persistence.PersonOperations;
import org.esfinge.querybuilder.util.DatabaseHSQLDBShutdown;
import org.esfinge.querybuilder.view.ApplicationView;

public class ApplicationController {

	private ApplicationView apl = new ApplicationView();

	private ContactOperations contactOperations = QueryBuilder
			.create(ContactOperations.class);

	private AddressOperations addressOperations = QueryBuilder
			.create(AddressOperations.class);

	private PersonOperations personOperations = QueryBuilder
			.create(PersonOperations.class);

	private boolean okContactName = true;
	private boolean okContactSurName = true;
	private boolean okContactBirthDate = true;
	private boolean okContacEmail = true;
	private boolean okContactPhoneNumber = true;
	private boolean okContactCity = true;
	private boolean okContactState = true;
	private boolean okContactStreetNumber = true;

	public void start() {

		this.apl.getBtnCancelContact().setEnabled(false);
		this.apl.getBtnSaveContact().setEnabled(false);
		this.apl.getBtnDeleteContact().setEnabled(false);
		this.apl.getBtnGoSearch().setEnabled(false);
		this.apl.getBtnSearchFriends().setEnabled(false);

		this.apl.disabledFields();

		this.addListeners();

		this.apl.setTableViewModel(buildJTableData(false));

		apl.setVisible(true);

	}

	private void addListeners() {
		this.apl.getBtnNewContact().addActionListener(
				new ClickButtonNewContact());
		this.apl.getBtnSaveContact().addActionListener(
				new ClickButtonSaveContact());
		this.apl.getBtnCancelContact().addActionListener(
				new ClickButtonCancelContact());
		this.apl.getBtnDeleteContact().addActionListener(
				new ClickButtonDeleteContact());
		this.apl.getBtnSearch().addActionListener(new ClickButtonSearch());

		this.apl.getBtnGoSearch().addActionListener(new ClickButtonGoSearch());

		this.apl.getBtnSearchFriends().addActionListener(
				new ClickButtonSearchFriends());

		this.apl.getBtnModifyContact().addActionListener(
				new ClickButtonModifyContact());
		this.apl.getBtnCloseApplication().addActionListener(
				new ClickButtonCloseApplication());
	}

	class ClickButtonNewContact implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			apl.getBtnCancelContact().setEnabled(true);
			apl.getBtnSearch().setEnabled(false);
			apl.getBtnSaveContact().setEnabled(true);
			apl.getBtnNewContact().setEnabled(false);
			apl.getBtnModifyContact().setEnabled(false);
			apl.getBtnDeleteContact().setEnabled(false);
			apl.enabledTableView(false);
			apl.enabledFields();
			apl.getTxtContactName().requestFocus();
			apl.cleanFields();
			apl.setOriginalLabelsColor();

		}

	}

	class ClickButtonCancelContact implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			apl.disabledFields();
			apl.cleanFields();
			apl.setOriginalLabelsColor();

			apl.getBtnCancelContact().setEnabled(false);
			apl.getBtnSaveContact().setEnabled(false);
			apl.getBtnSearch().setEnabled(true);
			apl.getBtnNewContact().setEnabled(true);
			apl.getBtnSearch().setEnabled(true);
			apl.getBtnModifyContact().setEnabled(true);
			apl.getBtnGoSearch().setEnabled(false);
			apl.getBtnSearchFriends().setEnabled(false);
			apl.getBtnDeleteContact().setEnabled(false);
			apl.setTableViewModel(buildJTableData(false));
			apl.enabledTableView(true);

		}

	}

	private int getNextContactId() {

		int contactId = 0;

		for (Contact contact : contactOperations
				.getContactOrderByContactIdDesc()) {

			contactId = contact.getContactId();
			break;

		}

		if (contactId == 0) {
			contactId = 1;
		} else {
			contactId++;
		}

		return contactId;
	}

	private int getNextPersonId() {

		int personId = 0;

		for (Person person : personOperations.getPersonOrderByPersonIdDesc()) {

			personId = person.getPersonId();
			break;

		}

		if (personId == 0) {
			personId = 1;
		} else {
			personId++;
		}

		return personId;
	}

	private int getNextAddressId() {

		int addressId = 0;

		for (Address address : addressOperations
				.getAddressOrderByAddressIdDesc()) {

			addressId = address.getAddressId();
			break;

		}

		if (addressId == 0) {
			addressId = 1;
		} else {
			addressId++;
		}

		return addressId;
	}

	class ClickButtonCloseApplication implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (apl.confirmMessage("Close Application", "Confirm?")) {
				System.exit(0);
			}

		}
	}

	class ClickButtonModifyContact implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (apl.getTableView().getSelectedRow() < 0) {
				apl.displayMessage(
						"Modify Contact",
						"Please select a contact in the table below before modifying",
						0);
				return;
			}

			int row = apl.getTableView().getSelectedRow();

			apl.getTxtContactId().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 0)));
			apl.getTxtContactName().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 1)));
			apl.getTxtContactSurName().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 2)));
			apl.getTxtContactBirthDate().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 3)));
			apl.getTxtContactEmail().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 4)));
			apl.getTxtContactPhoneNumber().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 5)));
			apl.getTxtContactCellNumber().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 6)));
			apl.getTxtContactStreet().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 7)));
			apl.getTxtContactStreetNumber().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 8)));
			apl.getTxtContactStreetComplement().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 9)));
			apl.getTxtContactZipcode().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 10)));
			apl.getTxtContactCity().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 11)));
			apl.getTxtContactState().setText(
					String.valueOf(apl.getTableView().getValueAt(row, 12)));
			apl.getCmbContactType().setSelectedItem(
					String.valueOf(apl.getTableView().getValueAt(row, 13)));

			if (apl.getTxtContactStreetNumber().getText().equals("null")) {
				apl.getTxtContactStreetNumber().setText("");
			}

			apl.enabledFields();
			apl.getBtnNewContact().setEnabled(false);
			apl.getBtnCancelContact().setEnabled(true);
			apl.getBtnSaveContact().setEnabled(true);
			apl.getBtnDeleteContact().setEnabled(true);
			apl.getBtnSearch().setEnabled(false);
			apl.getBtnGoSearch().setEnabled(false);
			apl.getBtnSearchFriends().setEnabled(false);
			apl.getBtnModifyContact().setEnabled(false);
			apl.getTableView().setEnabled(false);
			apl.enabledTableView(false);
			apl.getTxtContactName().requestFocus();

		}

	}

	class ClickButtonSaveContact implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (isValidContact()) {

				apl.getBtnCancelContact().setEnabled(false);
				apl.getBtnSaveContact().setEnabled(false);
				apl.getBtnSearch().setEnabled(true);
				apl.getBtnNewContact().setEnabled(true);
				apl.getBtnDeleteContact().setEnabled(false);
				apl.enabledTableView(true);
				apl.getBtnModifyContact().setEnabled(true);
				apl.getBtnNewContact().requestFocus();
				apl.disabledFields();

				Contact contact = fillContact();

				addressOperations.save(contact.getContactAddressId());
				personOperations.save(contact.getContactPersonId());
				contactOperations.save(contact);

				DatabaseHSQLDBShutdown.executeHSQLDBShutdown();

				apl.getTxtContactId().setText(
						String.valueOf(contact.getContactId()));
				apl.setTableViewModel(buildJTableData(false));

			} else {
				goFocus();
			}

		}

	}

	class ClickButtonSearch implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			apl.disabledFields();
			apl.cleanFields();
			apl.getTxtContactId().setEnabled(true);
			apl.getBtnNewContact().setEnabled(false);
			apl.getBtnCancelContact().setEnabled(true);
			apl.getBtnSearch().setEnabled(false);
			apl.getBtnGoSearch().setEnabled(true);
			apl.getBtnSearchFriends().setEnabled(true);
			apl.getBtnModifyContact().setEnabled(false);
			apl.getTableView().setEnabled(false);
			apl.enabledTableView(false);

			apl.getLblContactId().setForeground(Color.blue);
			apl.getLblContactName().setForeground(Color.blue);
			apl.getLblContactSurName().setForeground(Color.blue);
			apl.getLblContactCity().setForeground(Color.blue);
			apl.getLblContactState().setForeground(Color.blue);

			apl.getTxtContactId().setEnabled(true);
			apl.getTxtContactName().setEnabled(true);
			apl.getTxtContactSurName().setEnabled(true);
			apl.getTxtContactCity().setEnabled(true);
			apl.getTxtContactState().setEnabled(true);

			apl.getTxtContactId().requestFocus();

		}

	}

	class ClickButtonSearchFriends implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			Object[][] dataSearch = buildJTableData(true);

			if (dataSearch == null) {
				apl.displayMessage("Contact Search",
						"Mobile Friends Contacts not found.", 0);
				apl.getTxtContactId().requestFocus();
			} else {

				apl.disabledFields();
				apl.cleanFields();
				apl.setOriginalLabelsColor();

				apl.getBtnCancelContact().setEnabled(false);
				apl.getBtnSaveContact().setEnabled(false);
				apl.getBtnSearch().setEnabled(true);
				apl.getBtnNewContact().setEnabled(true);
				apl.getBtnSearch().setEnabled(true);
				apl.getBtnModifyContact().setEnabled(true);
				apl.getBtnGoSearch().setEnabled(false);
				apl.getBtnSearchFriends().setEnabled(false);
				apl.getBtnDeleteContact().setEnabled(false);
				apl.enabledTableView(true);

				apl.setTableViewModel(dataSearch);
			}

		}

	}

	class ClickButtonGoSearch implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if ((apl.getTxtContactId().getText().trim().length() == 0)
					&& (apl.getTxtContactName().getText().trim().length() == 0)
					&& (apl.getTxtContactSurName().getText().trim().length() == 0)
					&& (apl.getTxtContactCity().getText().trim().length() == 0)
					&& (apl.getTxtContactState().getText().trim().length() == 0)) {

				apl.getTxtContactId().requestFocus();
				return;
			}

			if ((apl.getTxtContactId().getText().trim().length() > 0)) {
				try {
					Integer.parseInt(apl.getTxtContactId().getText());
				} catch (Exception e2) {
					apl.displayMessage("Contact Search",
							"The ID field must be numeric.", 1);
					apl.getTxtContactId().requestFocus();
					return;

				}
			}

			Object[][] dataSearch = buildJTableDataSearch();

			if (dataSearch == null) {
				apl.displayMessage("Contact Search", "Contacts not found.", 0);
				apl.getTxtContactId().requestFocus();
			} else {

				apl.disabledFields();
				apl.cleanFields();
				apl.setOriginalLabelsColor();

				apl.getBtnCancelContact().setEnabled(false);
				apl.getBtnSaveContact().setEnabled(false);
				apl.getBtnSearch().setEnabled(true);
				apl.getBtnNewContact().setEnabled(true);
				apl.getBtnSearch().setEnabled(true);
				apl.getBtnModifyContact().setEnabled(true);
				apl.getBtnGoSearch().setEnabled(false);
				apl.getBtnSearchFriends().setEnabled(false);
				apl.getBtnDeleteContact().setEnabled(false);
				apl.enabledTableView(true);

				apl.setTableViewModel(dataSearch);
			}

		}

	}

	class ClickButtonDeleteContact implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (apl.confirmMessage("Delete Contact",
					"You confirm the removal of the contact?")) {

				Contact contact = fillContact();

				contactOperations.delete(contact.getContactId());
				addressOperations.delete(contact.getContactAddressId()
						.getAddressId());
				personOperations.delete(contact.getContactPersonId()
						.getPersonId());

				DatabaseHSQLDBShutdown.executeHSQLDBShutdown();

				apl.disabledFields();
				apl.cleanFields();
				apl.setOriginalLabelsColor();

				apl.getBtnCancelContact().setEnabled(false);
				apl.getBtnSaveContact().setEnabled(false);
				apl.getBtnSearch().setEnabled(true);
				apl.getBtnNewContact().setEnabled(true);
				apl.getBtnModifyContact().setEnabled(true);
				apl.getBtnGoSearch().setEnabled(false);
				apl.getBtnSearchFriends().setEnabled(false);
				apl.getBtnDeleteContact().setEnabled(false);
				apl.enabledTableView(true);
				apl.setTableViewModel(buildJTableData(false));

			} else {
				apl.getTxtContactName().requestFocus();
			}

		}

	}

	private void goFocus() {

		if (!okContactName) {
			apl.getTxtContactName().requestFocus();
			return;
		}

		if (!okContactSurName) {
			apl.getTxtContactSurName().requestFocus();
			return;
		}

		if (!okContactBirthDate) {
			apl.getTxtContactBirthDate().requestFocus();
			return;
		}

		if (!okContacEmail) {
			apl.getTxtContactEmail().requestFocus();
			return;
		}

		if (!okContactPhoneNumber) {
			apl.getTxtContactPhoneNumber().requestFocus();
			return;
		}

		if (!okContactStreetNumber) {
			apl.getTxtContactStreetNumber().requestFocus();
			return;
		}

		if (!okContactCity) {
			apl.getTxtContactCity().requestFocus();
			return;
		}

		if (!okContactState) {
			apl.getTxtContactState().requestFocus();
			return;
		}

	}

	private Person fillPerson(int idPerson) {

		Person person = new Person();

		person.setPersonName(apl.getTxtContactName().getText().trim()
				.toUpperCase());
		person.setPersonSurName(apl.getTxtContactSurName().getText().trim()
				.toUpperCase());
		person.setPersonBirthDate(apl.getTxtContactBirthDate().getText());

		if (idPerson == 0) {
			person.setPersonId(getNextPersonId());
		} else {
			person.setPersonId(idPerson);
		}

		return person;
	}

	private Address fillAddress(int idContact) {

		Address address = new Address();

		address.setAddressStreetNumber(apl.getTxtContactStreetNumber()
				.getText());

		if (idContact == 0) {
			address.setAddressId(getNextAddressId());
		} else {
			address.setAddressId(idContact);
		}

		address.setAddressCity(apl.getTxtContactCity().getText().trim()
				.toUpperCase());
		address.setAddressState(apl.getTxtContactState().getText().trim()
				.toUpperCase());
		address.setAddressStreet(apl.getTxtContactStreet().getText().trim()
				.toUpperCase());
		address.setAddressStreetComplement(apl.getTxtContactStreetComplement()
				.getText().trim().toUpperCase());

		if (apl.getTxtContactZipcode().getText().trim().length() == 1) {
			address.setAddressZipcode("");
		} else {
			address.setAddressZipcode(apl.getTxtContactZipcode().getText());
		}

		return address;
	}

	private Contact fillContact() {

		Contact contact = null;
		Person person = null;
		Address address = null;

		if (apl.getTxtContactId().getText().trim().length() > 0) {

			contact = contactOperations
					.getById(apl.getTxtContactId().getText());
			contact.setContactId(Integer.valueOf(apl.getTxtContactId()
					.getText()));

			person = fillPerson(contact.getContactPersonId().getPersonId());
			address = fillAddress(contact.getContactAddressId().getAddressId());

		} else {

			contact = new Contact();
			contact.setContactId(getNextContactId());

			person = fillPerson(0);
			address = fillAddress(0);

		}

		if (apl.getTxtContactCellNumber().getText().trim().length() == 10) {
			contact.setContactCellNumber(null);
		} else {
			contact.setContactCellNumber(apl.getTxtContactCellNumber()
					.getText());
		}

		contact.setContactEmail(apl.getTxtContactEmail().getText().trim()
				.toLowerCase());
		contact.setContactPhoneNumber(apl.getTxtContactPhoneNumber().getText());
		contact.setContactPersonId(person);
		contact.setContactAddressId(address);

		contact.setContactType(apl.getCmbContactType().getSelectedItem()
				.toString());

		return contact;

	}

	private boolean dateIsValid(String texto) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			sdf.parse(texto);
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}

	private boolean isValidContact() {

		boolean valid = true;

		okContactName = true;
		okContactSurName = true;
		okContactBirthDate = true;
		okContacEmail = true;
		okContactPhoneNumber = true;
		okContactCity = true;
		okContactState = true;
		okContactStreetNumber = true;

		if (apl.getTxtContactName().getText().trim().length() == 0
				|| apl.getTxtContactName().getText().trim().length() > 99) {
			apl.getLblContactName().setForeground(Color.red);
			valid = false;
			okContactName = false;
		} else {
			apl.getLblContactName().setForeground(Color.black);
		}

		if (apl.getTxtContactSurName().getText().trim().length() == 0
				|| apl.getTxtContactSurName().getText().trim().length() > 99) {
			apl.getLblContactSurName().setForeground(Color.red);
			valid = false;
			okContactSurName = false;
		} else {
			apl.getLblContactSurName().setForeground(Color.black);
		}

		if (apl.getTxtContactBirthDate().getText().trim().length() == 0
				|| apl.getTxtContactBirthDate().getText().trim().length() != 10
				|| dateIsValid(apl.getTxtContactBirthDate().getText()) == false) {

			apl.getLblContactBirthDate().setForeground(Color.red);
			valid = false;
			okContactBirthDate = false;
		} else {
			apl.getLblContactBirthDate().setForeground(Color.black);
		}

		if (apl.getTxtContactEmail().getText().trim().length() == 0
				|| apl.getTxtContactEmail().getText().trim().length() > 99
				|| !apl.getTxtContactEmail().getText().trim().contains("@")
				|| !apl.getTxtContactEmail().getText().trim().contains(".")) {
			apl.getLblContactEmail().setForeground(Color.red);
			valid = false;
			okContacEmail = false;
		} else {
			apl.getLblContactEmail().setForeground(Color.black);
		}

		if (apl.getTxtContactPhoneNumber().getText().trim().length() == 0
				|| apl.getTxtContactPhoneNumber().getText().trim().length() != 14) {
			apl.getLblContactPhoneNumber().setForeground(Color.red);
			valid = false;
			okContactPhoneNumber = false;
		} else {
			apl.getLblContactPhoneNumber().setForeground(Color.black);
		}

		if (apl.getTxtContactCity().getText().trim().length() == 0
				|| apl.getTxtContactCity().getText().trim().length() > 99) {
			apl.getLblContactCity().setForeground(Color.red);
			valid = false;
			okContactCity = false;
		} else {
			apl.getLblContactCity().setForeground(Color.black);
		}

		if (apl.getTxtContactState().getText().trim().length() == 0
				|| apl.getTxtContactState().getText().trim().length() != 2) {
			apl.getLblContactState().setForeground(Color.red);
			valid = false;
			okContactState = false;
		} else {
			apl.getLblContactState().setForeground(Color.black);
		}

		if (apl.getTxtContactStreetNumber().getText().trim().length() > 0) {

			try {
				int value = Integer.parseInt(apl.getTxtContactStreetNumber()
						.getText().trim());

				if (value == 0) {

					apl.getLblContactStreetNumber().setForeground(Color.red);
					valid = false;
					okContactStreetNumber = false;

				} else {

					apl.getLblContactStreetNumber().setForeground(Color.black);
				}

			} catch (Exception c) {
				apl.getLblContactStreetNumber().setForeground(Color.red);
				valid = false;
				okContactStreetNumber = false;
			}

		} else {
			apl.getLblContactStreetNumber().setForeground(Color.black);
		}

		return valid;
	}

	private int setTypeOfSearch() {

		int typeSearch = 0;

		if (apl.getTxtContactId().getText().trim().length() > 0) {

			typeSearch = 1;
		} else if ((apl.getTxtContactName().getText().trim().length() > 0
				&& apl.getTxtContactSurName().getText().trim().length() > 0
				&& apl.getTxtContactCity().getText().trim().length() > 0 && apl
				.getTxtContactState().getText().trim().length() > 0)
				||

				(apl.getTxtContactName().getText().trim().length() > 0 && apl
						.getTxtContactCity().getText().trim().length() > 0)

				|| (apl.getTxtContactName().getText().trim().length() > 0 && apl
						.getTxtContactState().getText().trim().length() > 0)

				||

				(apl.getTxtContactSurName().getText().trim().length() > 0

				&& apl.getTxtContactState().getText().trim().length() > 0)

				||

				(apl.getTxtContactSurName().getText().trim().length() > 0

				&& apl.getTxtContactCity().getText().trim().length() > 0)

		)

		{

			typeSearch = 2;

		} else if (apl.getTxtContactName().getText().trim().length() > 0
				|| apl.getTxtContactSurName().getText().trim().length() > 0) {

			typeSearch = 3;

		} else {

			typeSearch = 4;

		}

		return typeSearch;

	}

	private Object[][] buildJTableDataSearch() {

		int typeSerach = setTypeOfSearch();

		List<Contact> listContact = new ArrayList<Contact>();

		switch (typeSerach) {
		case 1:

			listContact.add(contactOperations.getById(apl.getTxtContactId()
					.getText()));

			if (listContact.get(0) == null) {
				return null;
			}
			break;
		case 2:

			List<Contact> listAuxContact = null;
			List<Person> listPerson = new ArrayList<Person>();
			listPerson = personOperations
					.getPersonByPersonNameStartsAndPersonSurNameStarts(
							apl.getTxtContactName().getText().trim()
									.toUpperCase(), apl.getTxtContactSurName()
									.getText().trim().toUpperCase());

			if (listPerson.size() > 0) {

				listAuxContact = new ArrayList<Contact>();
				listAuxContact = contactOperations.getContact();

				for (Person p : listPerson) {

					for (Contact c2 : listAuxContact) {

						if (c2.getContactPersonId().getPersonId() == p
								.getPersonId()) {
							listContact.add(c2);
						}

					}

				}
			}

			if (listContact.size() == 0) {
				return null;
			}

			List<Address> listAddress = new ArrayList<Address>();
			listAddress = addressOperations
					.getAddressByAddressCityStartsAndAddressStateStarts(
							apl.getTxtContactCity().getText().trim()
									.toUpperCase(), apl.getTxtContactState()
									.getText().trim().toUpperCase());

			if (listAddress.size() == 0) {
				return null;
			}

			listAuxContact.clear();

			for (Address a : listAddress) {

				for (int i = 0; i < listContact.size(); i++) {

					Contact contactAddress = listContact.get(i);

					if (contactAddress.getContactAddressId().getAddressId() == a
							.getAddressId()) {
						listAuxContact.add(contactAddress);
					}

				}

			}

			if (listAuxContact.size() == 0) {
				return null;
			}

			listContact.clear();

			for (Contact cFinal : listAuxContact) {
				listContact.add(cFinal);
			}

			break;
		case 3:
			List<Contact> listAuxContact3 = null;
			List<Person> listPerson3 = new ArrayList<Person>();
			listPerson3 = personOperations
					.getPersonByPersonNameStartsAndPersonSurNameStarts(
							apl.getTxtContactName().getText().trim()
									.toUpperCase(), apl.getTxtContactSurName()
									.getText().trim().toUpperCase());

			if (listPerson3.size() > 0) {

				listAuxContact3 = new ArrayList<Contact>();
				listAuxContact3 = contactOperations.getContact();

				for (Person p : listPerson3) {

					for (Contact c2 : listAuxContact3) {

						if (c2.getContactPersonId().getPersonId() == p
								.getPersonId()) {
							listContact.add(c2);

						}

					}

				}
			} else {
				return null;
			}
			break;
		case 4:

			List<Contact> listAuxContact4 = contactOperations.getContact();
			List<Address> listAddress4 = new ArrayList<Address>();
			listAddress4 = addressOperations
					.getAddressByAddressCityStartsAndAddressStateStarts(
							apl.getTxtContactCity().getText().trim()
									.toUpperCase(), apl.getTxtContactState()
									.getText().trim().toUpperCase());

			if (listAddress4.size() == 0) {
				return null;
			}

			for (Address a : listAddress4) {

				for (int i = 0; i < listAuxContact4.size(); i++) {

					Contact contactAddress = listAuxContact4.get(i);

					if (contactAddress.getContactAddressId().getAddressId() == a
							.getAddressId()) {
						listContact.add(contactAddress);
					}

				}

			}

			break;
		default:
			apl.displayMessage("Search Contact", "Invalid Type Search", 1);
			break;
		}

		Object[][] data = new Object[listContact.size()][this.apl.getAtm()
				.getColumnCount()];

		for (int row = 0; row < data.length; row++) {

			Contact c = listContact.get(row);

			for (int column = 0; column < this.apl.getAtm().getColumnCount(); column++) {

				Object dataValue = new Object();

				switch (column) {
				case 0:
					dataValue = c.getContactId();
					break;
				case 1:
					dataValue = c.getContactPersonId().getPersonName();
					break;
				case 2:
					dataValue = c.getContactPersonId().getPersonSurName();
					break;
				case 3:
					dataValue = c.getContactPersonId().getPersonBirthDate();
					break;
				case 4:
					dataValue = c.getContactEmail();
					break;
				case 5:
					dataValue = c.getContactPhoneNumber();
					break;
				case 6:
					dataValue = c.getContactCellNumber();
					break;
				case 7:
					dataValue = c.getContactAddressId().getAddressStreet();
					break;
				case 8:
					dataValue = c.getContactAddressId()
							.getAddressStreetNumber();

					break;
				case 9:
					dataValue = c.getContactAddressId()
							.getAddressStreetComplement();
					break;
				case 10:
					dataValue = c.getContactAddressId().getAddressZipcode();
					break;
				case 11:
					dataValue = c.getContactAddressId().getAddressCity();
					break;
				case 12:
					dataValue = c.getContactAddressId().getAddressState();
					break;
				case 13:
					dataValue = c.getContactType();
					break;

				}

				data[row][column] = dataValue;
				dataValue = null;

			}

			c = null;

		}

		return data;
	}

	private Object[][] buildJTableData(boolean searchMobileFriends) {

		List<Contact> listContact;

		if (searchMobileFriends) {

			listContact = contactOperations.getContactMobileFriends();
		} else {

			listContact = contactOperations.list();

		}

		Object[][] data = new Object[listContact.size()][this.apl.getAtm()
				.getColumnCount()];

		for (int row = 0; row < data.length; row++) {

			Contact c = listContact.get(row);

			for (int column = 0; column < this.apl.getAtm().getColumnCount(); column++) {

				Object dataValue = new Object();

				switch (column) {
				case 0:
					dataValue = c.getContactId();
					break;
				case 1:
					dataValue = c.getContactPersonId().getPersonName();
					break;
				case 2:
					dataValue = c.getContactPersonId().getPersonSurName();
					break;
				case 3:
					dataValue = c.getContactPersonId().getPersonBirthDate();
					break;
				case 4:
					dataValue = c.getContactEmail();
					break;
				case 5:
					dataValue = c.getContactPhoneNumber();
					break;
				case 6:
					dataValue = c.getContactCellNumber();
					break;
				case 7:
					dataValue = c.getContactAddressId().getAddressStreet();
					break;
				case 8:
					dataValue = c.getContactAddressId()
							.getAddressStreetNumber();

					break;
				case 9:
					dataValue = c.getContactAddressId()
							.getAddressStreetComplement();
					break;
				case 10:
					dataValue = c.getContactAddressId().getAddressZipcode();
					break;
				case 11:
					dataValue = c.getContactAddressId().getAddressCity();
					break;
				case 12:
					dataValue = c.getContactAddressId().getAddressState();
					break;
				case 13:
					dataValue = c.getContactType();
				}

				data[row][column] = dataValue;
				dataValue = null;

			}

			c = null;

		}

		return data;

	}

}
