package smokesignals.interfaces;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.parser.IParser;
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
import smokesignals.utils.FhirQuery;
import smokesignals.utils.RestServiceMockUtils;
import smokesignals.utils.fhirconverter.FhirConverterFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 2017-11-27.
 */
public abstract class DSTU2BaseTest {

    public static long CONNECTION_TIMEOUT_SHORT = 2;
    public static long CONNECTION_TIMEOUT_MED = 5;
    public static long CONNECTION_TIMEOUT_LONG = 10;

    private final String FHIRTEST_URL = "http://fhirtest.uhn.ca/baseDstu3/";
    private boolean mLiveTest = false;

    private MockWebServer mServer;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;

    protected FhirInterface mFhirInterface;
    protected ObjectMapper mFhirObjectMapper;
    protected IParser mFhirJsonParser;

    @Before
    public void setUp() throws Exception {

        FhirContext ctxDstu2 = FhirContext.forDstu2();

        mFhirObjectMapper = new ObjectMapper();
        mFhirJsonParser = ctxDstu2.newJsonParser();

        String derivedUrl;
        if (mLiveTest) {
            derivedUrl = FHIRTEST_URL;
            CONNECTION_TIMEOUT_SHORT = 10;
            CONNECTION_TIMEOUT_MED = 20;
            CONNECTION_TIMEOUT_LONG = 30;
        } else {
            derivedUrl = startMockServer();
        }

        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(derivedUrl)
                .addConverterFactory(FhirConverterFactory.create(ctxDstu2))
                .client(mOkHttpClient)
                .build();

        mFhirInterface = mRetrofit.create(FhirInterface.class);
    }

    @After
    public void tearDown() throws Exception {
        if (mServer != null) {
            mServer.shutdown();
        }
        testWithLiveServer(false);
    }

    /**
     * Test cases must generate their own dispatchers.
     */
    public abstract Dispatcher getDispatcher();

    /**
     * Spins up a mock server to receive HTTP REST requests.
     *
     * @return {@link String} URL of the mocked server.
     * @throws IOException
     */
    public String startMockServer() throws IOException {

        mServer = new MockWebServer();
        mServer.start();

        final Dispatcher dispatcher = getDispatcher();
        mServer.setDispatcher(dispatcher);

        return mServer.url("").toString();
    }

    /**
     * Returns the parsed JSON string read from the file indicated.
     *
     * @param fileName Name of the file to load JSON String from.
     * @return {@link String} read from file.
     */
    public String getJsonString(String fileName) {
        return RestServiceMockUtils.getStringFromFile(this.getClass().getClassLoader(), fileName);
    }

    /**
     * Sets the test cases to point to the live fhirtest.uhn.ca server or not. In reality, this should always be false...we
     * are testing against the mocked server, but sometimes it's good to check with the real world.
     *
     * @param pointToLiveServer
     */
    public void testWithLiveServer(boolean pointToLiveServer) {
        mLiveTest = pointToLiveServer;
    }
}