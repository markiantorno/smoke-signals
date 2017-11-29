package smokesignals.interfaces;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import smokesignals.utils.RestServiceMockUtils;
import smokesignals.utils.fhirconverter.FhirConverterFactory;

import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 2017-11-27.
 */
public class FhirInterfaceTest {

    private final String DUMMY_TOKEN = "757fb9c4-41ef-49f3-99f5-7d16285a5de0";
    private final String DUMMY_TOKEN_BEARER = "Bearer " + DUMMY_TOKEN;

    private final String ENDPOINT_OBSERVATION = "Observation";

    private String mObservationBundleResponseJson;
    private Bundle mObservationResponseResponse;

    private MockWebServer mServer;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private FhirInterface mFhirInterface;

    private final String FHIRTEST_URL = "http://fhirtest.uhn.ca/baseDstu3/";
    private boolean mLiveTest = true;//false;

    @Before
    public void setUp() throws Exception {

        FhirContext ctxDstu2 = FhirContext.forDstu2();

        ObjectMapper mapper = new ObjectMapper();
        IParser jsonParser = ctxDstu2.newJsonParser();

        mObservationBundleResponseJson = RestServiceMockUtils.getStringFromFile(this.getClass().getClassLoader(), "observation_bundle.json");
        mObservationResponseResponse = (Bundle) jsonParser.parseResource(mObservationBundleResponseJson);

        mServer = new MockWebServer();
        mServer.start();

        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                String endpoint = request.getPath().substring(1);
                if (endpoint.equals(ENDPOINT_OBSERVATION)) {
//                        Assert.assertTrue(request.getBody().readUtf8().equals(mChangePasswordRequestJson.replaceAll("\\s+| ", "")));
//                        Assert.assertTrue(request.getHeader(RestServiceMockUtils.HEADER_AUTHORIZATION).equals(DUMMY_TOKEN_BEARER));
                    return new MockResponse().setBody(mObservationBundleResponseJson).setResponseCode(HttpURLConnection.HTTP_OK);
                } else {
                    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN);
                }
            }
        };

        mServer.setDispatcher(dispatcher);

        HttpUrl baseUrl = mServer.url("");

        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(RestServiceMockUtils.CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS)
                .connectTimeout(RestServiceMockUtils.CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mLiveTest ? FHIRTEST_URL : baseUrl.toString())
                .addConverterFactory(FhirConverterFactory.create(ctxDstu2))
                .client(mOkHttpClient)
                .build();

        mFhirInterface = mRetrofit.create(FhirInterface.class);

    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

    @Test
    public void testObservationSearch() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        Call<Bundle> call = mFhirInterface.getResources(ENDPOINT_OBSERVATION);
        call.enqueue(new Callback<Bundle>() {
            public void onResponse(Call<Bundle> call, Response<Bundle> response) {
                if (response.isSuccessful()) {
                    latch.countDown();
                } else {
                    TestCase.fail("loginTest !isSuccessful : " + response.message());
                }
            }

            public void onFailure(Call<Bundle> call, Throwable throwable) {
                TestCase.fail("loginTest onFailure : " + throwable.getMessage());
            }
        });

        Assert.assertTrue(latch.await(RestServiceMockUtils.CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS));
    }

    @Test
    public void testObservationObservationRead() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        Call<BaseResource> call = mFhirInterface.read(Observation.class.getSimpleName(), "27099", null);

        call.enqueue(new Callback<BaseResource>() {
            public void onResponse(Call<BaseResource> call, Response<BaseResource> response) {
                if (response.isSuccessful()) {
                    latch.countDown();
                } else {
                    TestCase.fail("loginTest !isSuccessful : " + response.message());
                }
            }

            public void onFailure(Call<BaseResource> call, Throwable throwable) {
                TestCase.fail("loginTest onFailure : " + throwable.getMessage());
            }
        });

        Assert.assertTrue(latch.await(RestServiceMockUtils.CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS));
    }
}