package postalcode.service

import postalcode.model.State

import java.util.concurrent.*

import postalcode.model.PostalCode

import postalcode.util.*

import java.util.concurrent.FutureTask

/**
 * Geonames implementation of the PostalCodeService.
 * This implementation also caches results for improved
 * scalability and better user experience.
 */
public class GeonamesPostalCodeService implements PostalCodeService {

    private static String[][] DEFAULT_STATES = [
            ["AL","Alabama"],
            ["AK","Alaska"],
            ["AZ", "Arizona"],
            ["AR", "Arkansas"],
            ["CA", "California"],
            ["CO", "Colorado"],
            ["CT", "Connecticut"],
            ["DE", "Delaware"],
            ["FL", "Florida"],
            ["GA", "Georgia"],
            ["HI", "Hawaii"],
            ["ID", "Idaho"],
            ["IL", "Illinois"],
            ["IN", "Indiana"],
            ["IA", "Iowa"],
            ["KS", "Kansas"],
            ["KY", "Kentucky"],
            ["LA", "Louisiana"],
            ["ME", "Maine"],
            ["MD", "Maryland"],
            ["MA", "Massachusetts"],
            ["MI", "Michigan"],
            ["MN", "Minnesota"],
            ["MS", "Mississippi"],
            ["MO", "Missouri"],
            ["MT", "Montana"],
            ["NE", "Nebraska"],
            ["NV", "Nevada"],
            ["NH", "New Hampshire"],
            ["NJ", "New Jersey"],
            ["NM", "New Mexico"],
            ["NY", "New York"],
            ["NC", "North Carolina"],
            ["ND", "North Dakota"],
            ["OH", "Ohio"],
            ["OK", "Oklahoma"],
            ["OR", "Oregon"],
            ["PA", "Pennsylvania"],
            ["RI", "Rhode Island"],
            ["SC", "South Carolina"],
            ["SD", "South Dakota"],
            ["TN", "Tennessee"],
            ["TX", "Texas"],
            ["UT", "Utah"],
            ["VT", "Vermont"],
            ["VA", "Virginia"],
            ["WA", "Washington"],
            ["WV", "West Virginia"],
            ["WI", "Wisconsin"],
            ["WY", "Wyoming"],
            ];

    private ConcurrentHashMap<String, FutureTask> cache = new ConcurrentHashMap<String, FutureTask>()

    private final int max
    private final int threads
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
    GeonamesPostalCodeService(int max = 100000, int threads = 5, String username = "llinder", String[][] states = DEFAULT_STATES) {
        this.max = max
        this.threads = threads
        this.username = username
        this.states = states
    }

    @Override
    Future<List<State>> getSortedPostalCodeCounts() {
        final String cacheKey = "getSortedPostalCodeCounts"
        FutureTask<List<State>> value = cache.get(cacheKey)
        if(value == null) {
            // value not in cache, create and add it
            FutureTask<List<State>> newValue = createSortedPostalCodeCountsTask()
            value = cache.putIfAbsent(cacheKey, newValue)
            if(value == null) {
                // put succeeded, run new task
                value = newValue
                value.run()
            }
        }
        return value
    }

    private Future<List<State>> createSortedPostalCodeCountsTask() {
        GetSortedPostalCodeCounts callable = new GetSortedPostalCodeCounts(getPostalCodeSet())
        return new FutureTask<List<State>>(callable)
    }

    @Override
    void clear() {
        cache.clear()
    }

    private Future<Set<PostalCode>> getPostalCodeSet() {
        final String cacheKey = "getPostalCodeSet"
        FutureTask<Set<PostalCode>> value = cache.get(cacheKey)
        if(value == null) {
            // value not in cache, create and add it
            FutureTask<Set<PostalCode>> newValue = createPostalCodeSetTask()
            value = cache.putIfAbsent(cacheKey, newValue)
            if(value == null) {
                // put succeeded, run new task
                value = newValue
                value.run()
            }
        }
        return value
    }

    private FutureTask<Set<PostalCode>> createPostalCodeSetTask() {
        Callable<Set<PostalCode>> callable = new GetPostalCodes(max, threads, username, states)
        return new FutureTask<Set<PostalCode>>(callable)
    }
}