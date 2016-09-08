package net.sf.esfinge.querybuilder.model.persistence;

import java.util.List;

import net.sf.esfinge.querybuilder.model.Contact;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

@DomainTerm(term = "mobile friends", conditions = {
		@Condition(property = "contactEmail", comparison = ComparisonType.NOT_EQUALS, value = " "),
		@Condition(property = "contactType", comparison = ComparisonType.EQUALS, value = "FRIEND"),
		@Condition(property = "contactCellNumber", comparison = ComparisonType.NOT_EQUALS, value = " ") })
public interface ContactOperations extends Repository<Contact> {

	public List<Contact> getContact();

	public List<Contact> getContactOrderByContactIdDesc();

	public List<Contact> getContactMobileFriends();

}