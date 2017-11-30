package smokesignals.utils.parameters;

/**
 * Created by mark on 2017-11-29.
 */
public enum Modifiers {

    TEXT(":text"),
    NOT(":not"),
    ABOVE(":above"),
    BELOW(":below"),
    IN(":in"),
    NOT_IN("not-in");

    private final String mEncodedValue;

    Modifiers(String encodedValue) {
        mEncodedValue = encodedValue;
    }

    public String getEncodedValue() {
        return mEncodedValue;
    }


}
