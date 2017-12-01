package smokesignals.utils.query;

import com.sun.istack.internal.NotNull;
import smokesignals.utils.query.parameters.FhirParameter;
import smokesignals.utils.query.contract.QueryElement;

import java.util.AbstractMap;

import static smokesignals.utils.query.parameters.ResourceParameter.*;
import static smokesignals.utils.query.parameters.ResourceParameter.QUERY;
import static smokesignals.utils.query.parameters.ResourceParameter.SECURITY;
import static smokesignals.utils.query.parameters.ResultParameter.*;
import static smokesignals.utils.query.parameters.ResultParameter.AT;

@SuppressWarnings("unused")
public class Query implements QueryElement {

    private FhirParameter mParameter;
    private Object mValue;

    private Query(@NotNull FhirParameter parameter, @NotNull Object value) {
        mParameter = parameter;
        mValue = value;
    }

    @Override
    public AbstractMap.SimpleEntry<String, String> create() {
        return new AbstractMap.SimpleEntry<>(mParameter.getStringValue(), String.valueOf(mValue));
    }

    public static Query id(@NotNull String id) {
        return new Query(ID, id);
    }

    public static Query lastUpdated(@NotNull String lastUpdated) {
        return new Query(LAST_UPDATED, lastUpdated);
    }

    public static Query settag(@NotNull String tag) {
        return new Query(TAG, tag);
    }

    public static Query profile(@NotNull String profile) {
        return new Query(PROFILE, profile);
    }

    public static Query security(@NotNull String security) {
        return new Query(SECURITY, security);
    }

    public static Query text(@NotNull String text) {
        return new Query(TEXT, text);
    }

    public static Query content(@NotNull String content) {
        return new Query(CONTENT, content);
    }

    public static Query list(@NotNull String list) {
        return new Query(LIST, list);
    }

    public static Query has(@NotNull String has) {
        return new Query(HAS, has);
    }

    public static Query type(@NotNull String type) {
        return new Query(TYPE, type);
    }

    public static Query query(@NotNull String query) {
        return new Query(QUERY, query);
    }

    public static Query sort(@NotNull String sort) {
        return new Query(SORT, sort);
    }

    public static Query count(@NotNull int count) {
        return new Query(COUNT, String.valueOf(count));
    }

    public static Query include(@NotNull String include) {
        return new Query(SECURITY, include);
    }

    public static Query revinclude(@NotNull String revinclude) {
        return new Query(REVINCLUDE, revinclude);
    }

    public static Query summary(@NotNull String summary) {
        return new Query(SUMMARY, summary);
    }

    public static Query elements(@NotNull String elements) {
        return new Query(ELEMENTS, elements);
    }

    public static Query contained(@NotNull String contained) {
        return new Query(CONTAINED, contained);
    }

    public static Query containedType(@NotNull String containedType) {
        return new Query(CONTAINED_TYPE, containedType);
    }

    public static Query since(@NotNull String since) {
        return new Query(SECURITY, since);
    }

    public static Query at(@NotNull String at) {
        return new Query(AT, at);
    }
}
