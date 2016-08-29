package org.esfinge.querybuilder.jpa1.custommethods;

public class CustomMethodImpl implements CustomMethodInterface{
	
	public static boolean methodInvoked = false;

	@Override
	public void customMethod() {
		methodInvoked = true;		
	}

}
