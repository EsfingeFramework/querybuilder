package org.esfinge.querybuilder.jdbc.integration.worker;

import java.util.List;

import org.esfinge.querybuilder.jdbc.testresources.Worker;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.Starts;

public interface  TestQueryWorker extends Repository<Worker>{
	public List<Worker> getWorker();
	public Worker getWorkerById(Integer id);
	public List<Worker> getWorkerByLastName(String lastname);
	public Worker getWorkerByNameAndLastName(String name, String lastname);
	public List<Worker> getWorkerByNameOrLastName(String name, String lastname);
	public List<Worker> getWorkerByAddressCity(String city);
	public List<Worker> getWorkerByLastNameAndAddressState(String lastname, String state);
	public List<Worker> getWorkerByAge(@Greater Integer age);
	public List<Worker> getWorkerByAgeLesser(Integer age);
	public List<Worker> getWorkerByLastNameNotEquals(String name);
	public List<Worker> getWorkerByName(@Starts String name);
	public List<Worker> getWorkerByNameStartsAndAgeGreater(String name, Integer age);
	public List<Worker> getWorkerOrderByName();
	public List<Worker> getWorkerByAgeOrderByNameDesc(@Greater Integer age);
}