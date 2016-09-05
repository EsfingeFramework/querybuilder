package net.sf.esfinge.querybuilder.jdbc.domainterms.worker;

import java.util.List;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.DomainTerms;
import net.sf.esfinge.querybuilder.jdbc.testresources.Worker;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

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