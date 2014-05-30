package org.esfinge.querybuilder.jdbc.domainterms.worker;

import java.util.List;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.DomainTerms;
import org.esfinge.querybuilder.jdbc.testresources.Worker;
import org.esfinge.querybuilder.methodparser.ComparisonType;

@DomainTerm(term="teenager",
conditions={@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="13"),
            @Condition(property="age",comparison=ComparisonType.LESSER_OR_EQUALS,value="19")})
@DomainTerms({	
	@DomainTerm(term="old guys", conditions=@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="65"))
})		 

public interface TestDomainQueryWorker extends Repository<Worker> {
	
	public List<Worker> getWorkerById(Integer Id);
	public List<Worker> getWorkerTeenager();
	public List<Worker> getWorkerOldGuys();
}