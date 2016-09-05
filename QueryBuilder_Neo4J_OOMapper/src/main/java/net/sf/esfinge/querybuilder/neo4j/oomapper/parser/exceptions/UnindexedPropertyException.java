package net.sf.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class UnindexedPropertyException extends RuntimeException {

	private String message;
	
	public UnindexedPropertyException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
