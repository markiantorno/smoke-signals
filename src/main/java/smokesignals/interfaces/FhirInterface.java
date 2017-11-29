package smokesignals.interfaces;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by mark on 2017-11-27.
 */
public interface FhirInterface {

    //SEARCH
    /**
     * Search for most recent instances of given resource.
     */
    @GET
    Call<Bundle> getResources(@Url String endpoint);


    //READ
    /**
     * Read an individual resource instance given its ID (and optionally a version ID to retrieve a specific version of
     * that instance to vread that instance)
     */

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
