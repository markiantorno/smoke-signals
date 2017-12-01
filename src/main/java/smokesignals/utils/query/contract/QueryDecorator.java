package smokesignals.utils.query.contract;

import java.util.AbstractMap;

public class QueryDecorator implements QueryElement {

    QueryElement mQueryElement;

    public QueryDecorator(QueryElement queryElement) {
        this.mQueryElement = queryElement;
    }

    @Override
    public AbstractMap.SimpleEntry<String, String> create() {
        return this.mQueryElement.create();
    }
}
