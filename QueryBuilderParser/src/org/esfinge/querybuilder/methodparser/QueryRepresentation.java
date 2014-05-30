package org.esfinge.querybuilder.methodparser;

import java.util.Map;
import java.util.Set;

public interface QueryRepresentation {
	
	public boolean isDynamic();
	
	public Object getQuery();
	
	public Object getQuery(Map<String, Object> params);

	public Set<String> getFixParameters();

	public Object getFixParameterValue(String param);

}
