package smokesignals.utils.query;

import smokesignals.utils.query.contract.QueryElement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FhirQuery extends HashMap<String, String> {

    private FhirQuery() {}

    public static class FhirQueryBuilder {
        List<AbstractMap.SimpleEntry<String, String>> mQueries = new ArrayList<>();

        public FhirQueryBuilder where(QueryElement element) {
            mQueries.add(element.create());
            return this;
        }

        public FhirQuery build() {
             FhirQuery queryMap = new FhirQuery();
             for(SimpleEntry<String, String> entry : mQueries) {
                 queryMap.put(entry.getKey(), entry.getValue());
             }
             return queryMap;
        }
    }
}
