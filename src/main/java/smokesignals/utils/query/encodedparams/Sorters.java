package smokesignals.utils.query.encodedparams;

/**
 * Created by mark on 2017-11-29.
 */
public enum Sorters {

    ASCENDING(""),
    DECENDING("-");

    private final String mEncodedValue;

    Sorters(String encodedValue) {
        mEncodedValue = encodedValue;
    }

    public String getEncodedValue() {
        return mEncodedValue;
    }


}
