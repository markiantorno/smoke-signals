package smokesignals.interfaces;

import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * Created by mark on 2017-11-27.
 */
public interface FhirInterface {

    String AUTHORIZATION = "Authorization";

    //SEARCH
    /**
     * Search for most recent instances of given resource.
     */
    @GET
    Call<Bundle> getResources(@Url String endpoint);


    //READ
    /**
     * The read interaction accesses the current contents of a resource.
     * The interaction is performed by an {@code HTTP GET} command as shown:
     *  <p>
     * {@code GET [base]/[type]/[id] {?_format=[mime-type]} }
     *  <p>
     * This returns a single instance with the content specified for the resource type. This url may be accessed by a
     * browser. The possible values for the Logical Id ("id") itself are described in the id type. The returned resource
     * SHALL have an id element with a value that is the [id]. Servers SHOULD return an ETag header with the versionId
     * of the resource (if versioning is supported) and a Last-Modified header.
     * <p>
     * Note: Unknown resources and deleted resources are treated differently on a read: a GET for a deleted resource
     * returns a {@code 410} status code, whereas a GET for an unknown resource returns {@code 404}. Systems that do not
     * track deleted records will treat deleted records as an unknown resource. Since deleted resources may be brought
     * back to life, servers MAY include an ETag on the error response when reading a deleted record to allow version
     * contention management when a resource is brought back to life.
     * <p>
     * In addition, the search parameter _summary can be used when reading a resource:
     * <p>
     * {@code GET [base]/[type]/[id] {?_summary=text}}
     * <p>
     * This requests that only a subset of the resource content be returned, as specified in the _summary parameter,
     * which can have the values true, false, text & data. Note that a resource that only contains a subset of the data
     * is not suitable for use as a base to update the resource, and may not be suitable for other uses. The same
     * applies to the _elements parameter - both that it should be supported, and the subset implications. Servers
     * SHOULD define a Resource.meta.tag with the SUBSETTED as a Simple Tag to explicitly mark such resources.
     *
     * @param resourceType
     * @param answer_id
     * @param bearer
     */
    @GET("{type}/{id}")
    Call<BaseResource> read(@Path("type") String resourceType,
                            @Path("id") String answer_id,
                            @Header(AUTHORIZATION) String bearer);

    //VREAD
    /**
     * The vread interaction preforms a version specific read of the resource. The interaction is performed by an
     * {@code HTTP GET} command as shown:
     * <p>
     * {@code GET [base]/[type]/[id]/_history/[vid] {?_format=[mime-type]}}
     * <p>
     * This returns a single instance with the content specified for the resource type for that version of the resource.
     * The returned resource SHALL have an id element with a value that is the [id], and a meta.versionId element with a
     * value of [vid]. Servers SHOULD return an ETag header with the versionId (if versioning is supported) and a
     * Last-Modified header.\
     * <p>
     * The Version Id ("vid") is an opaque identifier that conforms to the same format requirements as a Logical Id.
     * The id may have been found by performing a history interaction (see below), by recording the version id from a
     * content location returned from a read or from a version specific reference in a content model. If the version
     * referred to is actually one where the resource was deleted, the server should return a 410 status code.
     * <p>
     * Servers are encouraged to support a version specific retrieval of the current version of the resource even if
     * they do not provide access to previous versions. If a request is made for a previous version of a resource, and
     * the server does not support accessing previous versions (either generally, or for this particular resource), it
     * should return a {@code 404} Not Found error, with an operation outcome explaining that history is not supported
     * for the underlying resource type or instance.
     *
     * @param resourceType
     * @param answer_id
     * @param version
     * @param bearer
     */
    @GET("{type}/{id}/_history/{vid}")
    Call<BaseResource> vRead(@Path("type") String resourceType,
                             @Path("id") String answer_id,
                             @Path("vid") String version,
                             @Header(AUTHORIZATION) String bearer);

    //HISTORY
    /**
     * Retrieve the update history across the Patient resource type, or against a specific instance of this resource
     * type if an ID is specified.
     */

    //DELETE
    /**
     * Delete an individual instance of the resource.
     */

    //CREATE
    /**
     * Create an instance of the resource. Generally you do not need to specify an ID but you may force the server to
     * use a specific ID by including one.
     */

    //UPDATE
    /**
     * Update an existing instance of the resource by ID.
     */

    //VALIDATE
    /**
     * Validate an instance of the resource to check whether it would be acceptable for creating/updating, without
     * actually storing it on the server.
     */

}
