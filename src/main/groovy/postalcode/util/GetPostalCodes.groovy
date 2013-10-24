package postalcode.util

import groovy.json.JsonSlurper

import java.util.concurrent.Callable
import postalcode.model.PostalCode

import static groovyx.gpars.GParsPool.withPool

/**
 * Java callable implementation that queries the Geonames service
 * and returns a set of all postal codes for the given list of states.
 */
class GetPostalCodes implements Callable<Set<PostalCode>> {

    private static String[][] DEFAULT_STATES = [["MN","Minnesota"], ["TX","Texas"], ["CA", "California"]]

    private final int max
    private final int threadCount
    private final String username
    private final String[][] states

    /**
     * Default constructor
     *
     * @param max Maximum number of postal codes to retrieve for each state
     * @param threadCount Number of threads to use for concurrent service calls
     * @param username Registered user for the Geonames service
     * @param states Array of US state tuples to retrieve
     */
    GetPostalCodes(int max = 100000, int threadCount = 5, String username = "llinder", String[][] states = GetPostalCodes.DEFAULT_STATES) {
        this.max = max
        this.threadCount = threadCount
        this.username = username
        this.states = states
    }

    @Override
    Set<PostalCode> call() throws Exception {

        // configure new GPars execution pool
        withPool(this.threadCount) {

            // start parallel retrieval of postal code information
            final List<PostalCode> list = states.parallel
                // map JSON response to list of PostalCode values
                .map { state ->
                    String json = new URL("http://api.geonames.org/postalCodeLookupJSON?country=US&maxRows=$max&username=$username&placename=${state[1]}").text
                    // not sure if JsonSlurper is thread safe so make new instance just in case.
                    JsonSlurper slurper = new JsonSlurper()
                    List pc = slurper.parseText(json)["postalcodes"]
                    pc = pc.collect { new PostalCode(it["postalcode"], it["adminName1"], it["adminCode1"]) }
                    //filter out mismatches returned by geonames placename search.
                    pc.findAll { it.abbr == state[0] }
                }
                // convert to groovy collection and flatten results
                .collection.flatten()

            return new HashSet<PostalCode>( list )
        }
    }
}
