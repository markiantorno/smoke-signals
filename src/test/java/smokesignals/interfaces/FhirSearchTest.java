package smokesignals.interfaces;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import junit.framework.TestCase;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smokesignals.utils.query.FhirQuery;
import smokesignals.utils.query.Query;

import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 2017-11-27.
 */
public class FhirSearchTest extends DSTU2BaseTest {

    private final String DUMMY_TOKEN = "757fb9c4-41ef-49f3-99f5-7d16285a5de0";
    private final String DUMMY_TOKEN_BEARER = "Bearer " + DUMMY_TOKEN;

    private final String ENDPOINT_OBSERVATION = "Observation";
    private final String ENDPOINT_OBSERVATION_COUNT_TWO = "Observation?_count=2";

    private String mObservationBaseSearchResponseJson;
    private Bundle mObservationBaseSearchResponse;

    private String mObservationCountLimitedSearchResponseJson;
    private Bundle mObservationCountLimitedSearchResponse;

    @Before
    public void setUp() throws Exception {
        testWithLiveServer(true);
        super.setUp();

        mObservationBaseSearchResponseJson = getJsonString("search/observation_base_search_result.json");
        mObservationBaseSearchResponse = (Bundle) mFhirJsonParser.parseResource(mObservationBaseSearchResponseJson);

        mObservationCountLimitedSearchResponseJson = getJsonString("search/observation_count_limited_search_result.json");
        mObservationCountLimitedSearchResponse = (Bundle) mFhirJsonParser.parseResource(mObservationCountLimitedSearchResponseJson);
    }

    @Override
    public Dispatcher getDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                String endpoint = request.getPath().substring(1);
                String methodHTTP = request.getMethod();

                switch (endpoint) {
                    case ENDPOINT_OBSERVATION:
                        if (methodHTTP.equals(HTTPVerbEnum.GET.name())) {
                            return new MockResponse().setBody(mObservationBaseSearchResponseJson)
                                    .setResponseCode(HttpURLConnection.HTTP_OK);
                        } else {
                            return new MockResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN);
                        }
                    case ENDPOINT_OBSERVATION_COUNT_TWO:
                        if (methodHTTP.equals(HTTPVerbEnum.GET.name())) {
                            return new MockResponse().setBody(mObservationCountLimitedSearchResponseJson)
                                    .setResponseCode(HttpURLConnection.HTTP_OK);
                        } else {
                            return new MockResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN);
                        }
                    default:
                        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN);
                }
            }
        };
    }

    @Test
    public void testObservationSearch() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        Call<Bundle> call = mFhirInterface.search(ENDPOINT_OBSERVATION, new FhirQuery.FhirQueryBuilder().build());
        call.enqueue(new Callback<Bundle>() {
            public void onResponse(Call<Bundle> call, Response<Bundle> response) {
                if (response.isSuccessful()) {
                    if (!isLiveTest()) {
                        ReflectionAssert.assertReflectionEquals(response.body().getEntry(),
                                mObservationBaseSearchResponse.getEntry());
                    }
                    latch.countDown();
                } else {
                    TestCase.fail("loginTest !isSuccessful : " + response.message());
                }
            }

            public void onFailure(Call<Bundle> call, Throwable throwable) {
                TestCase.fail("loginTest onFailure : " + throwable.getMessage());
            }
        });

        Assert.assertTrue(latch.await(CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS));
    }

    @Test
    public void testLimitedObservationSearch() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        final int numberRequestedResults = 2;

        FhirQuery fhirQuery = new FhirQuery.FhirQueryBuilder()
                .where(Query.count(numberRequestedResults))
                .build();

        Call<Bundle> call = mFhirInterface.search(ENDPOINT_OBSERVATION, fhirQuery);
        call.enqueue(new Callback<Bundle>() {
            public void onResponse(Call<Bundle> call, Response<Bundle> response) {
                if (response.isSuccessful()) {
                    if (isLiveTest()) {
                        Assert.assertEquals(numberRequestedResults, response.body().getEntry().size());
                    } else {
                        ReflectionAssert.assertReflectionEquals(response.body().getEntry(),
                                mObservationCountLimitedSearchResponse.getEntry());
                    }
                    latch.countDown();
                } else {
                    TestCase.fail("loginTest !isSuccessful : " + response.message());
                }
            }

            public void onFailure(Call<Bundle> call, Throwable throwable) {
                TestCase.fail("loginTest onFailure : " + throwable.getMessage());
            }
        });

        Assert.assertTrue(latch.await(CONNECTION_TIMEOUT_SHORT, TimeUnit.SECONDS));
    }
}

