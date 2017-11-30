package smokesignals.utils.parameters;

/**
 * For the ordered parameter types of number, date, and quantity, a prefix to the parameter value may be used to control
 * the nature of the matching. To avoid URL escaping and visual confusion, the following prefixes are used.
 *
 * Created by mark on 2017-11-29.
 */
public enum Prefixes {

    EQUAL("eq"),
    NOT_EQUAL("ne"),
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_OR_EQUAL("ge"),
    LESS_OR_EQUAL("le"),
    STARTS_AFTER("sa"),
    ENDS_BEFORE("eb"),
    APPROX("ap");

    private final String mEncodedValue;

    Prefixes(String encodedValue) {
        mEncodedValue = encodedValue;
    }

    public String getEncodedValue() {
        return mEncodedValue;
    }

}