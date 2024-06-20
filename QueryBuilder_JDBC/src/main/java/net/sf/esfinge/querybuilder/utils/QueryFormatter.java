package net.sf.esfinge.querybuilder.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryFormatter {

    public String putValuesOnQuery(String query, Object[] valuesOfQuery,
            Map<String, Object> fixParameters) {

        Set<String> domainTerms = new HashSet<>();
        domainTerms = fixParameters.keySet();
        var contParameters = 0;

        if (!domainTerms.isEmpty()) {

            var it = domainTerms.iterator();
            while (it.hasNext()) {
                contParameters = (contParameters + 1);
                var domainTerm = it.next();
                query = query.replace(domainTerm,
                        encapsulateValue(fixParameters.get((domainTerm))));
            }
        }
        if (valuesOfQuery != null && valuesOfQuery.length > 0) {

            for (Object valuesOfQuery1 : valuesOfQuery) {
                if (valuesOfQuery1 == null) {
                    continue;
                }
                contParameters = (contParameters + 1);
                query = query.replace(contParameters + "?", encapsulateValue(valuesOfQuery1));
            }
        }

        query = putParenthesesOnQuery(query);
        query = replaceNullOptions(query);
        return query;
    }

    private String encapsulateValue(Object obj) {

        var builder = new StringBuilder();
        var tipo = obj.getClass().getSimpleName().toUpperCase();
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

        var builderQuery = new StringBuilder();

        if ((query.contains(" or ") || query.contains(" OR "))
                || (query.contains(" LIKE ") || query.contains(" like "))) {

            query = query.replace("''", "' '");
            var parts = query.split(" ");
            var contOrPosition = 0;
            var contLikePosition = 0;
            var orPositions = new int[parts.length];
            var likePositions = new int[parts.length];

            List<String> listReplaces = new ArrayList<>();

            for (var i = 0; i < parts.length; i++) {
                if (parts[i].equalsIgnoreCase("or")) {
                    orPositions[contOrPosition] = i;
                    contOrPosition++;
                }
                if (parts[i].equalsIgnoreCase("LIKE")) {
                    likePositions[contLikePosition] = i;
                    contLikePosition++;
                }

            }

            for (var i = 0; i < contOrPosition; i++) {

                parts[(orPositions[i] - 3)] = "(" + parts[(orPositions[i] - 3)];
                parts[(orPositions[i] + 3)] = parts[(orPositions[i] + 3)] + ")";

            }

            for (String part : parts) {
                if (part.length() == 0) {
                    continue;
                }
                builderQuery.append(part + " ");
            }

            String queryReplace = null;

            for (var i = 0; i < listReplaces.size(); i++) {

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
