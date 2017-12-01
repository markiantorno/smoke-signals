package smokesignals.utils.query.decorator;

import smokesignals.utils.query.contract.QueryDecorator;
import smokesignals.utils.query.contract.QueryElement;
import smokesignals.utils.query.encodedparams.Compare;

import java.util.AbstractMap;

public class Comparator extends QueryDecorator {

    final Compare mPrefix;

    private Comparator(QueryElement queryElement, Compare mPrefix) {
        super(queryElement);
        this.mPrefix = mPrefix;
    }

    public static QueryElement add(QueryElement queryElement, Compare mPrefix) {
        return new Comparator(queryElement, mPrefix);
    }

    @Override
    public AbstractMap.SimpleEntry<String, String> create() {
        AbstractMap.SimpleEntry<String, String> entry = super.create();
        entry.setValue(mPrefix.getEncodedValue() + entry.getValue());
        return entry;
    }
}
