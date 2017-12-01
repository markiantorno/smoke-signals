package smokesignals.utils.query.parameters;

/**
 * Searching for resources is fundamental to the mechanics of FHIR. Search operations traverse through an existing set
 * of resources filtering by parameters supplied to the search operation.
 * <p>
 * These values represent the possible search parameters for results.
 */
@SuppressWarnings("unused")
public enum ResultParameter implements FhirParameter {

    SORT("_sort"),
    COUNT("_count"),
    INCLUDE("_include"),
    REVINCLUDE("_revinclude"),
    SUMMARY("_summary"),
    ELEMENTS("_elements"),
    CONTAINED("_contained"),
    CONTAINED_TYPE("_containedType"),
    SINCE("_since"),
    AT("_at");

    private final String mEncodedValue;

    ResultParameter(String encodedValue) {
        mEncodedValue = encodedValue;
    }

    @Override
    public String getStringValue() {
        return mEncodedValue;
    }

    @Override
    public String toString() {
        return mEncodedValue;
    }
}
