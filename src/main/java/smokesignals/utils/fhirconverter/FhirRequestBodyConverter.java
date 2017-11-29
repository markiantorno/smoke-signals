package smokesignals.utils.fhirconverter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.parser.IParser;
import com.sun.istack.internal.NotNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * Created by mark on 2017-11-28.
 */
public class FhirRequestBodyConverter<T extends BaseResource> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final IParser fhirJsonParser;

    public FhirRequestBodyConverter(@NotNull FhirContext fhirContext) {
        fhirJsonParser = fhirContext.newJsonParser();
    }

    @Override
    public RequestBody convert(T t) throws IOException {
        byte[] bytes = fhirJsonParser.encodeResourceToString(t).getBytes();
        return RequestBody.create(MEDIA_TYPE, bytes);
    }
}
