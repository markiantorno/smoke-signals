package smokesignals.utils.fhirconverter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.parser.IParser;
import com.sun.istack.internal.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * Created by mark on 2017-11-28.
 */
public class FhirResponseBodyConverter<T extends BaseResource> implements Converter<ResponseBody, T> {

    private final IParser fhirJsonParser;

    public FhirResponseBodyConverter(@NotNull FhirContext fhirContext) {
        fhirJsonParser = fhirContext.newJsonParser();
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        try {
            return (T) fhirJsonParser.parseResource(responseBody.charStream());
        } finally {
            responseBody.close();
        }
    }
}
