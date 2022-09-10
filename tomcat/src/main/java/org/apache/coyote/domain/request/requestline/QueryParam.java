package org.apache.coyote.domain.request.requestline;

import java.util.HashMap;
import java.util.Map;

public class QueryParam {

    private static final String QUERY_STRING_REGEX = "&";
    private static final String QUERY_MAP_DELIMITER = "=";
    private static final String EMPTY_QUERY_STRING = "";

    private final Map<String, String> parameters;

    private QueryParam(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public static QueryParam from(String queryString) {
        Map<String, String> queryMap = new HashMap<>();
        if (queryString.equals(EMPTY_QUERY_STRING)) {
            return new QueryParam(queryMap);
        }
        String[] queryParams = queryString.split(QUERY_STRING_REGEX);
        for (String queryParam : queryParams) {
            String[] splitQuery = queryParam.split(QUERY_MAP_DELIMITER);
            queryMap.put(splitQuery[0], splitQuery[1]);
        }
        return new QueryParam(queryMap);
    }

    public String getQueryValue(String key) {
        return parameters.get(key);
    }
}
