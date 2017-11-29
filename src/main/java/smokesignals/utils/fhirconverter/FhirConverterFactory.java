package smokesignals.utils.fhirconverter;

import ca.uhn.fhir.context.FhirContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by mark on 2017-11-28.
 */
public class FhirConverterFactory extends Converter.Factory {

    /**
     * Create an instance using {@code FhirContext} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static FhirConverterFactory create(FhirContext dstu2) {
        if (dstu2 == null) throw new NullPointerException("FhirContext == null");
        return new FhirConverterFactory(dstu2);
    }

    private final FhirContext mFhirContext;

    private FhirConverterFactory(FhirContext dstu2) {
        this.mFhirContext = dstu2;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new FhirResponseBodyConverter<>(mFhirContext);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FhirRequestBodyConverter<>(mFhirContext);
    }
}