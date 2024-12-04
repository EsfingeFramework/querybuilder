package org.esfinge.querybuilder.utils;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Object> {

	@Override
	public int compare(Object obj1, Object obj2) {
		int priority1 = ServiceLocator.getObjectPriority(obj1);
		int priority2 = ServiceLocator.getObjectPriority(obj2);
		return priority2 - priority1;
	}

}
