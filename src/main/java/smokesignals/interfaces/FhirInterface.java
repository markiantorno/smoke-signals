package smokesignals.interfaces;

import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import retrofit2.Call;
import retrofit2.http.*;

import ca.uhn.fhir.model.dstu2.resource.*;

import java.util.Map;

/**
 * Created by mark on 2017-11-27.
 */
public interface FhirInterface {

    String AUTHORIZATION = "Authorization";

    /**
     * This interaction searches a set of resources based on some filter criteria. The interaction can be performed by
     * several different HTTP commands.
     * <p>
     * {@code GET [base]/[type]{?[parameters]{&_format=[mime-type]}}}
     * This searches all resources of a particular type using the criteria represented in the parameters.
     * <p>
     * Because of the way that some user agents and proxies treat {@code GET} and {@code POST} requests, in addition
     * to the get based search method above, servers that support search SHALL also support a POST based search:
     * <p>
     * {@code POST [base]/[type]/_search{?[parameters]{&_format=[mime-type]}}}
     * <p>
     * This has exactly the same semantics as the equivalent {@code GET} command. All these search interactions take a series
     * of parameters that are a series of name=value pairs encoded in the URL (or as an
     * application/x-www-form-urlencoded submission for a POST). (See W3C HTML forms ).
     * <p>
     * Note: application/x-www-form-urlencoded is supported for POST so that invoking a search by {@code GET} and
     * {@code POST} can be done from HTML forms in a browser (though considerable active content might be required in
     * the browser), although this is not the main usage.
     * <p>
     * Searches are processed as specified for the Search handling mechanism.
     * <p>
     * If the search succeeds, the server SHALL return a {@code 200 OK} HTTP status code and the return content SHALL
     * be a {@link Bundle} with type = searchset containing the results of the search as a collection of zero or more resources
     * in a defined order. The result collection can be long, so servers may use paging. If they do, they SHALL use the
     * method described below (adapted from RFC 5005 (Feed Paging and Archiving ) for breaking the collection into pages
     * if appropriate. The server MAY also return an {@link OperationOutcome} resource within the searchset {@link Bundle} entries that
     * contains additional information about the search; if one is sent it SHALL NOT include any issues with a fatal
     * or error severity, and it SHALL be marked with a Bundle.entry.search.mode of outcome.
     * <p>
     * If the search fails (cannot be executed, not that there is no matches), the return value is a status code 4xx
     * or 5xx with an {@link OperationOutcome}.
     * <p>
     * Common HTTP Status codes returned on FHIR-related errors (in addition to normal HTTP errors related to security,
     * header and content type negotiation issues):
     * <ul>
     * <li>400 Bad Request - search could not be processed or failed basic FHIR validation rules</li>
     * <li>401 Not Authorized - authorization is required for the interaction that was attempted</li>
     * <li>404 Not Found - resource type not supported, or not a FHIR end-point</li>
     * </ul>
     *
     * @param endpoint
     * @param searchPrameters
     * @return
     */
    @GET
    Call<Bundle> search(@Url String endpoint,
                        @QueryMap Map<String, String> searchPrameters);

    /**
     * The read interaction accesses the current contents of a resource.
     * The interaction is performed by an {@code HTTP GET} command as shown:
     * <p>
     * {@code {@code GET} [base]/[type]/[id] {?_format=[mime-type]} }
     * <p>
     * This returns a single instance with the content specified for the resource type. This url may be accessed by a
     * browser. The possible values for the Logical Id ("id") itself are described in the id type. The returned resource
     * SHALL have an id element with a value that is the [id]. Servers SHOULD return an ETag header with the versionId
     * of the resource (if versioning is supported) and a Last-Modified header.
     * <p>
     * Note: Unknown resources and deleted resources are treated differently on a read: a {@code GET} for a deleted resource
     * returns a {@code 410} status code, whereas a {@code GET} for an unknown resource returns {@code 404}. Systems that do not
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
     * @param id
     * @param bearer
     */
    @GET("{type}/{id}")
    Call<BaseResource> read(@Path("type") String resourceType,
                            @Path("id") String id,
                            @Header(AUTHORIZATION) String bearer);

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
     * @param id
     * @param version
     * @param bearer
     */
    @GET("{type}/{id}/_history/{vid}")
    Call<BaseResource> vRead(@Path("type") String resourceType,
                             @Path("id") String id,
                             @Path("vid") String version,
                             @Header(AUTHORIZATION) String bearer);

    /**
     * The update interaction creates a new current version for an existing resource or creates an initial version if
     * no resource already exists for the given id. The update interaction is performed by an HTTP PUT command as shown:
     * <p>
     * {@code PUT [base]/[type]/[id] {?_format=[mime-type]}}
     * <p>
     * The request body SHALL be a Resource with an id element that has an identical value to the [id] in the URL. If
     * no id element is provided, or the value is wrong, the server SHALL respond with an {@code HTTP 400} error code, and
     * SHOULD provide an {@link OperationOutcome} identifying the issue. If the request body includes a meta, the server SHALL
     * ignore the provided versionId and lastUpdated values. If the server supports versions, it SHALL populate the
     * meta.versionId and meta.lastUpdated with the new correct values. Servers are allowed to review and alter the
     * other metadata values, but SHOULD refrain from doing so (see metadata description for further information). Note
     * that there is no support for updating past versions - see notes on the history interaction.
     * <p>
     * A server SHOULD accept the resource as submitted when it accepts the update, and return the same content when it
     * is subsequently read. However systems may not be able to do this; see the note on transactional integrity for
     * discussion. Also, see Variations between Submitted data and Retrieved data for additional discussion around
     * update behavior. Note that update generally updates the whole content of the resource. For partial updates,
     * see patch below.
     * <p>
     * If the interaction is successful, the server SHALL return either a {@code 200 OK HTTP} status code if the
     * resource was updated, or a {@code 201} Created status code if the resource was created, with a Last-Modified header,
     * and an ETag header which contains the new versionId of the resource. If the resource was created (i.e. the
     * interaction resulted in a {@code 201} Created), the server SHOULD return a Location header (this is for HTTP
     * conformance; it's not otherwise needed).
     * <p>
     * Note: Servers MAY choose to preserve XML comments, instructions, and formatting or JSON whitespace when accepting
     * updates, but are not required to do so. The impact of this on digital signatures may need to be considered.
     * <p>
     * Note that servers MAY choose to allow clients to PUT a resource to a location that does not yet exist on the
     * server - effectively, allowing the client to define the id of the resource. Whether a server allows this is a
     * deployment choice based on the nature of its relationships with the clients. While many servers will not allow
     * clients to define their ids, there are several reasons why it may be necessary in some configurations:
     * <ul>
     * <li>client is reproducing an existing data model on the server, and needs to keep original ids in order to
     * retain ongoing integrity</li>
     * <li>client is a server doing push based pub/sub (this is a special case of the first reason)</li>
     * <li>multiple clients doing push in the context of agreed data model shared across multiple servers where ids are
     * shared across servers</li>
     * </ul>
     * Alternatively, clients may be sharing an agreed identification model (e.g. key server, scoped identifiers, or
     * UUIDs) where clashes do not arise.
     * <p>
     * Servers can choose whether or not to support client defined ids, and indicate such to the clients using
     * {@code CapabilityStatement.rest.resource.updateCreate}.
     * <p>
     * Servers are permitted to reject update interactions because of integrity concerns or other business rules, and
     * return {@code HTTP} status codes accordingly (usually a {@code 422}.
     * <p>
     * Common HTTP Status codes returned on FHIR-related errors (in addition to normal {@code HTTP} errors related to
     * security, header and content type negotiation issues):
     * <ul>
     * <li> {@code 400 Bad Request} - resource could not be parsed or failed basic FHIR validation rules (or multiple
     * matches were found for conditional criteria)
     * <li> {@code 401 Not Authorized} - authorization is required for the interaction that was attempted
     * <li> {@code 404 Not Found} - resource type not supported, or not a FHIR end-point
     * <li> {@code 405 Method Not allowed} - the resource did not exist prior to the update, and the server does not
     * allow client defined ids
     * <li> {@code 409/412 version conflict management} - see below
     * <li> {@code 422 Unprocessable Entity} - the proposed resource violated applicable FHIR profiles or server
     * business rules
     * </ul>
     * Any of these errors SHOULD be accompanied by an {@link OperationOutcome} resource providing additional detail concerning
     * the issue.
     * <p>
     * For additional information on how systems may behave when processing updates, refer to the Variations between
     * Submitted data and Retrieved data page.
     *
     * @param resourceType
     * @param id
     * @param bearer
     */
    @PUT("{type}/{id}")
    Call<BaseResource> update(@Path("type") String resourceType,
                              @Path("id") String id,
                              @Body BaseResource body,
                              @Header(AUTHORIZATION) String bearer);

    /**
     * The conditional update interaction allows a client to update an existing resource based on some identification
     * criteria, rather than by logical id. To accomplish this, the client issues a PUT as shown:
     * <p>
     * {@code PUT [base]/[type]?[search parameters]}
     * <p>
     * When the server processes this update, it performs a search using its standard search facilities for the resource
     * type, with the goal of resolving a single logical id for this request. The action it takes depends on how many
     * matches are found:
     * <ul>
     * <li>No matches: The server performs a create interaction</li>
     * <li>One Match: The server performs the update against the matching resource</li>
     * <li>Multiple matches: The server returns a {@code 412} Precondition Failed error indicating the client's criteria
     * were not selective enough</li>
     * </ul>
     * This variant can be used to allow a stateless client (such as an interface engine) to submit updated results to a
     * server, without having to remember the logical ids that the server has assigned. For example, a client updating
     * the status of a lab result from "preliminary" to "final" might submit the finalized result using
     * {@code PUT path/Observation?identifier=http://my-lab-system|123}
     * <p>
     * Note that transactions and conditional create/update/delete are complex interactions and it is not expected that
     * every server will implement them. Servers that don't support the conditional update should return an
     * {@code HTTP 400} error and an {@link OperationOutcome}.
     *
     * @param resourceType
     * @param searchPrameters
     * @param bearer
     * @return
     */
    @PUT("{type}")
    Call<BaseResource> conditionalUpdate(@Path("type") String resourceType,
                                         @QueryMap Map<String, String> searchPrameters,
                                         @Body BaseResource body,
                                         @Header(AUTHORIZATION) String bearer);

    /**
     * As an alternative to updating an entire resource, clients can perform a patch operation. This can be useful when
     * a client is seeking to minimise its bandwidth utilization, or in scenarios where a client has only partial access
     * or support for a resource. The patch interaction is performed by an HTTP PATCH command as shown:
     * <p>
     * {@code PATCH [base]/[type]/[id] {?_format=[mime-type]}}
     * The body of a PATCH operation SHALL be either:
     * <ul>
     * <li>a JSON Patch  document with a content type of {@code application/json-patch+json}</li>
     * <li>an XML Patch  document with a content type of {@code application/xml-patch+xml}</li>
     * <li>a FHIRPath Patch parameters resource with FHIR Content Type</li>
     * </ul>
     * In either case, the server SHALL process its own copy of the resource in the format indicated, applying the
     * operations specified in the document, following the relevant PATCH specification. When the operations have all
     * been processed, the server processes the resulting document as an Update operation; all the version and error
     * handling etc. applies as specified, as does the Prefer Header.
     * <p>
     * Processing PATCH operations may be very version sensitive. For this reason, servers SHALL support
     * conditional PATCH, which works exactly the same as specified for update in Concurrency Management. Clients SHOULD
     * always consider using version specific PATCH operations so that inappropriate actions are not executed. In
     * addition, servers SHALL support Conditional PATCH, which works exactly as described for Conditional Update.
     * <p>
     * The server SHALL ensure that the narrative in a resource is not clinically unsafe after the PATCH operation is
     * performed. Exactly how this is defined and can be achieved depends on the context, and how narrative is being
     * maintained, but servers may wish to consider:
     * <ul>
     * <li>If the existing narrative has a status != generated, the server could reject the PATCH operation</li>
     * <li>The server could regenerate the narrative once the operation has been applied to the data</li>
     * <li>In some limited circumstances, an XML PATCH operation could update the narrative</li>
     * <li>The server could delete the narrative, on the basis that some later process will be able to populate it correctly</li>
     * </ul>
     * Processing XML Patch documents is tricky because of namespace handling. Servers SHALL handle namespaces
     * correctly, but note that FHIR resources only contain two XML namespaces, for FHIR (http://hl7.org/fhir) and XHTML
     * (http://www.w3.org/1999/xhtml).
     * <p>
     * Patch operations may be performed as part of Batch or Transaction Operations using the FHIRPath Patch format.
     */
    @PATCH("{type}/{id}")
    Call<BaseResource> patch(@Path("type") String resourceType,
                             @Path("id") String id,
                             @Body BaseResource body,
                             @Header(AUTHORIZATION) String bearer);

    /**
     * The delete interaction removes an existing resource. The interaction is performed by an {@code HTTP DELETE}
     * command as shown:
     * <p>
     * {@code DELETE [base]/[type]/[id]}
     * <p>
     * A delete interaction means that subsequent non-version specific reads of a resource return a {@code 410 HTTP}
     * status code and that the resource is no longer found through search interactions. Upon successful deletion, or
     * if the resource does not exist at all, the server should return either a 200 OK if the response contains a
     * payload, or a 204 No Content with no response payload.
     * <p>
     * Whether to support delete at all, or for a particular resource type or a particular instance is at the
     * discretion of the server based on the business rules that apply in its context. If the server refuses to delete
     * resources of that type as a blanket policy, then it should return the 405 Method not allowed status code. If the
     * server refuses to delete a resource because of reasons specific to that resource, such as referential integrity,
     * it should return the {@code 409 Conflict} status code. Performing this interaction on a resource that is already
     * deleted has no effect, and the server should return either a 200 OK if the response contains a payload, or a
     * {@code 204 No Content} with no response payload. Resources that have been deleted may be "brought back to life"
     * by a subsequent update interaction using an {@code HTTP PUT}.
     * <p>
     * Many resources have a status element that overlaps with the idea of deletion. Each resource type defines what
     * the semantics of the deletion interactions are. If no documentation is provided, the deletion interaction should
     * be understood as deleting the record of the resource, with nothing about the state of the real-world
     * corresponding resource implied.
     * For servers that maintain a version history, the delete interaction does not remove a resource's version history.
     * From a version history respect, deleting a resource is the equivalent of creating a special kind of history
     * entry that has no content and is marked as deleted. Note that there is no support for deleting past versions -
     * see notes on the history interaction.
     * <p>
     * Since deleted resources may be brought back to life, servers MAY include an ETag on the delete response to allow
     * version contention management when a resource is brought back to life.
     *
     * @param id
     */
    @DELETE("{type}/{id}")
    Call<BaseResource> delete(@Path("type") String resourceType,
                              @Path("id") String id,
                              @Header(AUTHORIZATION) String bearer);

    /**
     * The conditional delete interaction allows a client to delete an existing resource based on some selection
     * criteria, rather than by a specific logical id. To accomplish this, the client issues an HTTP DELETE as shown:
     * <p>
     * {@code DELETE [base]/[type]/?[search parameters]}
     * <p>
     * When the server processes this delete, it performs a search as specified using the standard search facilities
     * for the resource type. The action it takes depends on how many matches are found:
     * <ul>
     * <li>No matches or One Match: The server performs an ordinary delete on the matching resource</li>
     * <li>Multiple matches: Servers may choose to delete all the matching resources, or it may choose to return a
     * {@code 412 Precondition Failed} error indicating the client's criteria were not selective enough. A server
     * indicates whether it can delete multiple resources in its Capability Statement
     * (.rest.resource.conditionalDelete). If there are multiple matches, either all must be deleted, or the server
     * SHALL return an error</li>
     * </ul>
     * This variant can be used to allow a stateless client (such as an interface engine) to delete a resource on a
     * server, without having to remember the logical ids that the server has assigned. For example, a client deleting
     * a lab atomic result might delete the resource using
     * {@code DELETE /Observation?identifier=http://my-lab-system|123.}
     * <p>
     * Note that transactions and conditional create/update/delete are complex interactions and it is not expected that
     * every server will implement them. Servers that don't support the conditional delete should return an
     * {@code HTTP 400} error and an {@link OperationOutcome}.
     */
    @DELETE("{type}")
    Call<BaseResource> conditionalDelete(@Path("type") String resourceType,
                                         @QueryMap Map<String, String> searchPrameters,
                                         @Header(AUTHORIZATION) String bearer);

    //CREATE

    /**
     * The create interaction creates a new resource in a server-assigned location. If the client wishes to have control
     * over the id of a newly submitted resource, it should use the update interaction instead. The create interaction
     * is performed by an {@code HTTP POST} command as shown:
     * <p>
     * {@code POST [base]/[type] {?_format=[mime-type]}}
     * <p>
     * The request body SHALL be a FHIR Resource. The resource does not need to have an id element (this is one of the
     * few cases where a resource exists without an id element). If an id is provided, the server SHALL ignore it. If
     * the request body includes a meta, the server SHALL ignore the existing versionId and lastUpdated values. The
     * server SHALL populate the id, meta.versionId and meta.lastUpdated with the new correct values. Servers are
     * allowed to review and alter the other metadata values, but SHOULD refrain from doing so (see metadata
     * description for further information).
     * <p>
     * A server SHOULD otherwise accept the resource as submitted when it accepts the create, and return the same
     * content when it is subsequently read. However some systems may not be able to do this; see the note on
     * transactional integrity for discussion.
     * <p>
     * The server returns a {@code 201 Created} HTTP status code, and SHALL also return a Location header which
     * contains the new Logical Id and Version Id of the created resource version:
     * <p>
     * {@code Location: [base]/[type]/[id]/_history/[vid]}
     * <p>
     * where {@code [id]} and {@code [vid]} are the newly created id and version id for the resource version. Servers
     * SHOULD return an ETag header with the versionId (if versioning is supported) and a Last-Modified header.
     * <p>
     * When the resource syntax or data is incorrect or invalid, and cannot be used to create a new resource, the server
     * returns a {@code 400 Bad Request} HTTP status code. When the server rejects the content of the resource because
     * of business rules, the server returns a {@code 422 Unprocessable Entity} error HTTP status code. In either case,
     * the server SHOULD include a response body containing an {@link OperationOutcome} with detailed error messages
     * describing the reason for the error.
     * <p>
     * Common HTTP Status codes returned on FHIR-related errors (in addition to normal HTTP errors related to security,
     * header and content type negotiation issues):
     * {@code 400 Bad Request} - resource could not be parsed or failed basic FHIR validation rules
     * {@code 404 Not Found} - resource type not supported, or not a FHIR end-point
     * {@code 422 Unprocessable Entity} - the proposed resource violated applicable FHIR profiles or server business
     * rules. This should be accompanied by an {@link OperationOutcome} resource providing additional detail
     * <p>
     * Note: Servers MAY choose to preserve XML comments, instructions, and formatting or JSON whitespace when
     * accepting creates, but are not required to do so. The impact of this on digital signatures may need to be
     * considered.
     * <p>
     * For additional information on how systems may behave when processing updates, refer to the Variations between
     * Submitted data and Retrieved data page.
     */
    @POST("{type}")
    Call<BaseResource> create(@Path("type") String resourceType,
                              @Body BaseResource body,
                              @Header(AUTHORIZATION) String bearer);

    /**
     * The conditional create interaction allows a client to create a new resource only if some equivalent resource
     * does not already exist on the server. The client defines what equivalence means in this case by supplying a FHIR
     * search query using an HL7 defined extension header "If-None-Exist" as shown:
     * <p>
     * {@code If-None-Exist: [search parameters]}
     * <p>
     * The parameter just contains the search parameters (what would be in the URL following the "?").
     * <p>
     * When the server processes this create, it performs a search as specified using its standard search facilities for
     * the resource type. The action it takes depends on how many matches are found:
     * <ul>
     * <li>No matches: The server processes the create as above</li>
     * <li>One Match: The server ignore the post and returns {@code 200 OK}</li>
     * <li>Multiple matches: The server returns a 412 Precondition Failed error indicating the client's criteria
     * were not selective enough</li>
     * </ul>
     * This variant can be used to avoid the risk of two clients creating duplicate resources for the same record.
     * For example, a client posting a new lab result might specify
     * {@code If-None-Exist: identifier=http://my-lab-system|123} to ensure it is does not create a duplicate record.
     * <p>
     * Note that transactions and conditional create/update/delete are complex interactions and it is not expected that
     * every server will implement them. Servers that don't support the conditional create should return an
     * {@code HTTP 412} error and an {@link OperationOutcome}.
     *
     * @param resourceType
     * @param body
     * @param searchPrameters
     * @param bearer
     * @return
     */
    @POST("{type}")
    @Headers({
            "If-None-Exist: {search_params}"
    })
    Call<BaseResource> conditionalCreate(@Path("type") String resourceType,
                                         @Body BaseResource body,
                                         @Path("search_params") Map<String, String> searchPrameters,
                                         @Header(AUTHORIZATION) String bearer);

}

