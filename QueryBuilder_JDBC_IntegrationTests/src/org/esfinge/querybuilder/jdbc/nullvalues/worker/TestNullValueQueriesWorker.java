package org.esfinge.querybuilder.jdbc.nullvalues.worker;

import java.util.List;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.Starts;
import org.esfinge.querybuilder.jdbc.testresources.Worker;

public interface TestNullValueQueriesWorker {

	public List<Worker> getWorkerByName(@CompareToNull String name);

	public List<Worker> getWorkerByNameAndLastName(@Starts String name,
			@CompareToNull String lastname);

	public List<Worker> getWorkerByAgeGreater(@IgnoreWhenNull Integer age);

	public List<Worker> getWorkerByNameStartsAndLastNameStarts(
			@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

}
