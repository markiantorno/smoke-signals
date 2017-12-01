package smokesignals.utils.query.decorator;

import smokesignals.utils.query.contract.QueryDecorator;
import smokesignals.utils.query.contract.QueryElement;
import smokesignals.utils.query.encodedparams.Modifier;

import java.util.AbstractMap;

public class Filter extends QueryDecorator {

    final Modifier mModifier;

    private Filter(QueryElement queryElement, Modifier mModifier) {
        super(queryElement);
        this.mModifier = mModifier;
    }

    public static QueryElement add(QueryElement queryElement, Modifier mModifier) {
        return new Filter(queryElement, mModifier);
    }

    @Override
    public AbstractMap.SimpleEntry<String, String> create() {
        AbstractMap.SimpleEntry<String, String> entry = super.create();
        entry = new AbstractMap.SimpleEntry<String, String>(entry.getKey() + mModifier.getEncodedValue(), entry.getValue());
        return entry;
    }
}
