package org.esfinge.querybuilder.model.persistence;

import java.util.List;

import org.esfinge.querybuilder.Repository;

import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.model.Contact;

@DomainTerm(term = "mobile friends", conditions = {
		@Condition(property = "contactEmail", comparison = ComparisonType.NOT_EQUALS, value = " "),
		@Condition(property = "contactType", comparison = ComparisonType.EQUALS, value = "FRIEND"),
		@Condition(property = "contactCellNumber", comparison = ComparisonType.NOT_EQUALS, value = " ") })
public interface ContactOperations extends Repository<Contact> {

	public List<Contact> getContact();

	public List<Contact> getContactOrderByContactIdDesc();

	public List<Contact> getContactMobileFriends();

}