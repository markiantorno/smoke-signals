package smokesignals.utils.parameters;

/**
 * Searching for resources is fundamental to the mechanics of FHIR. Search operations traverse through an existing set
 * of resources filtering by parameters supplied to the search operation.
 * <p>
 * These values represent the possible search parameters for resources.
 */
public enum ResourceParameter {

    ID("_id"),
    LAST_UPDATED("_lastUpdated"),
    TAG("_tag"),
    PROFILE("_profile"),
    SECURITY("_security"),
    TEXT("_text"),
    CONTENT("_content"),
    LIST("_list"),
    HAS("_has"),
    TYPE("_type"),
    QUERY("_query");

    private final String mEncodedValue;

    ResourceParameter(String encodedValue) {
        mEncodedValue = encodedValue;
    }

    public String getEncodedValue() {
        return mEncodedValue;
    }

}
