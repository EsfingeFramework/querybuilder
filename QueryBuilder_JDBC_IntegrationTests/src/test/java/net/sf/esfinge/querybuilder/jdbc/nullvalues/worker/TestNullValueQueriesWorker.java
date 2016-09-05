package net.sf.esfinge.querybuilder.jdbc.nullvalues.worker;

import java.util.List;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.Starts;
import net.sf.esfinge.querybuilder.jdbc.testresources.Worker;

public interface TestNullValueQueriesWorker {

	public List<Worker> getWorkerByName(@CompareToNull String name);

	public List<Worker> getWorkerByNameAndLastName(@Starts String name,
			@CompareToNull String lastname);

	public List<Worker> getWorkerByAgeGreater(@IgnoreWhenNull Integer age);

	public List<Worker> getWorkerByNameStartsAndLastNameStarts(
			@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

}
