package smokesignals.utils;

import java.util.HashMap;
import java.util.Map;

import static smokesignals.utils.parameters.ResourceParameter.*;
import static smokesignals.utils.parameters.ResultParameter.*;

public class FhirQuery extends HashMap<String, String> {

    @SuppressWarnings("unused")
    public static class FhirQueryBuilder {
        private Map<String, String> mQueryMap = new HashMap<>();

        public FhirQuery build () {
            FhirQuery query = new FhirQuery();
            query.putAll(mQueryMap);
            mQueryMap.clear();
            return query;
        }

        public void reset() {
            mQueryMap.clear();
        }

        public FhirQueryBuilder id(String id) {
            updateFieldValue(ID.getEncodedValue(), id);
            return this;
        }

        public FhirQueryBuilder lastUpdated(String lastUpdated) {
            updateFieldValue(LAST_UPDATED.getEncodedValue(), lastUpdated);
            return this;
        }

        public FhirQueryBuilder settag(String tag) {
            updateFieldValue(TAG.getEncodedValue(), tag);
            return this;
        }

        public FhirQueryBuilder profile(String profile) {
            updateFieldValue(PROFILE.getEncodedValue(), profile);
            return this;
        }

        public FhirQueryBuilder security(String security) {
            updateFieldValue(SECURITY.getEncodedValue(), security);
            return this;
        }

        public FhirQueryBuilder text(String text) {
            updateFieldValue(TEXT.getEncodedValue(), text);
            return this;
        }

        public FhirQueryBuilder content(String content) {
            updateFieldValue(CONTENT.getEncodedValue(), content);
            return this;
        }

        public FhirQueryBuilder list(String list) {
            updateFieldValue(LIST.getEncodedValue(), list);
            return this;
        }

        public FhirQueryBuilder has(String has) {
            updateFieldValue(HAS.getEncodedValue(), has);
            return this;
        }

        public FhirQueryBuilder type(String type) {
            updateFieldValue(TYPE.getEncodedValue(), type);
            return this;
        }

        public FhirQueryBuilder query(String query) {
            updateFieldValue(QUERY.getEncodedValue(), query);
            return this;
        }

        public FhirQueryBuilder sort(String sort) {
            updateFieldValue(SORT.getEncodedValue(), sort);
            return this;
        }

        public FhirQueryBuilder count(String count) {
            updateFieldValue(COUNT.getEncodedValue(), count);
            return this;
        }

        public FhirQueryBuilder include(String include) {
            updateFieldValue(SECURITY.getEncodedValue(), include);
            return this;
        }

        public FhirQueryBuilder revinclude(String revinclude) {
            updateFieldValue(REVINCLUDE.getEncodedValue(), revinclude);
            return this;
        }

        public FhirQueryBuilder summary(String summary) {
            updateFieldValue(SUMMARY.getEncodedValue(), summary);
            return this;
        }

        public FhirQueryBuilder elements(String elements) {
            updateFieldValue(ELEMENTS.getEncodedValue(), elements);
            return this;
        }

        public FhirQueryBuilder contained(String contained) {
            updateFieldValue(CONTAINED.getEncodedValue(), contained);
            return this;
        }

        public FhirQueryBuilder containedType(String containedType) {
            updateFieldValue(CONTAINED_TYPE.getEncodedValue(), containedType);
            return this;
        }

        public FhirQueryBuilder since(String since) {
            updateFieldValue(SECURITY.getEncodedValue(), since);
            return this;
        }

        public FhirQueryBuilder at(String at) {
            updateFieldValue(AT.getEncodedValue(), at);
            return this;
        }

        private  void updateFieldValue(String key, String value) {
            if (value == null) {
                mQueryMap.remove(key);
            } else {
                mQueryMap.put(key, value);
            }
        }
    }
}
    
