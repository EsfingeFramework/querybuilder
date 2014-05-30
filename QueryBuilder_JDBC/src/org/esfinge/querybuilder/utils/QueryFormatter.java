package org.esfinge.querybuilder.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryFormatter {

	public String putValuesOnQuery(String query, Object[] valuesOfQuery,
			Map<String, Object> fixParameters) {

		Set<String> domainTerms = new HashSet<String>();
		domainTerms = fixParameters.keySet();
		int contParameters = 0;

		if (!domainTerms.isEmpty()) {

			Iterator<String> it = domainTerms.iterator();
			while (it.hasNext()) {
				contParameters = (contParameters + 1);
				String domainTerm = it.next();
				query = query.replace(domainTerm,
						encapsulateValue(fixParameters.get((domainTerm))));
			}
		}
		if (valuesOfQuery != null && valuesOfQuery.length > 0) {

			for (int i = 0; i < valuesOfQuery.length; i++) {
				if (valuesOfQuery[i] == null) {
					continue;
				}
				contParameters = (contParameters + 1);
				query = query.replace(contParameters + "?",
						encapsulateValue(valuesOfQuery[i]));
			}
		}

		query = putParenthesesOnQuery(query);
		query = replaceNullOptions(query);
		return query;
	}

	private String encapsulateValue(Object obj) {

		StringBuilder builder = new StringBuilder();
		String tipo = obj.getClass().getSimpleName().toUpperCase();
		if (tipo.equals("STRING")) {
			builder.append("'" + obj.toString().trim() + "'");
		} else {
			builder.append(obj);
		}
		return builder.toString();
	}

	private String replaceNullOptions(String query) {

		query = query.replace("and and", "and");
		query = query.replace("where  and", "where");
		return query;
	}

	private String putParenthesesOnQuery(String query) {

		StringBuilder builderQuery = new StringBuilder();

		if ((query.contains(" or ") || query.contains(" OR "))
				|| (query.contains(" LIKE ") || query.contains(" like "))) {

			query = query.replace("''", "' '");
			String parts[] = query.split(" ");

			int contOrPosition = 0;
			int contLikePosition = 0;

			int[] orPositions = new int[parts.length];
			int[] likePositions = new int[parts.length];

			List<String> listReplaces = new ArrayList<String>();

			for (int i = 0; i < parts.length; i++) {
				if (parts[i].equalsIgnoreCase("or")) {
					orPositions[contOrPosition] = i;
					contOrPosition++;
				}
				if (parts[i].equalsIgnoreCase("LIKE")) {
					likePositions[contLikePosition] = i;
					contLikePosition++;
				}

			}

			for (int i = 0; i < contOrPosition; i++) {

				parts[(orPositions[i] - 3)] = "(" + parts[(orPositions[i] - 3)];
				parts[(orPositions[i] + 3)] = parts[(orPositions[i] + 3)] + ")";

			}

			for (int j = 0; j < parts.length; j++) {

				if (parts[j].length() == 0) {
					continue;
				}

				builderQuery.append(parts[j] + " ");

			}

			String queryReplace = null;

			for (int i = 0; i < listReplaces.size(); i++) {

				if (i % 2 == 0) {

					query = query.replace(listReplaces.get(i),
							listReplaces.get(i + 1));
					queryReplace = query;

				}

			}

			if (listReplaces.size() > 0) {
				return queryReplace;
			} else {

				return builderQuery.toString();

			}
		}

		return query;
	}
}
