package postalcode.util

import postalcode.model.PostalCode

import java.util.concurrent.*

import postalcode.model.State

/**
 * Java callable implementation that converts a set of
 * postal codes to a list of states with postal code
 * count for each state.
 */
class GetSortedPostalCodeCounts implements Callable<List<State>> {

    private final Future<Set<PostalCode>> futureCodes

    GetSortedPostalCodeCounts(Future<Set<PostalCode>> futureCodes) {
        this.futureCodes = futureCodes
    }

    @Override
    List<State> call() throws Exception {
        // get set of all postal codes
        Set<PostalCode> codes = futureCodes.get()

        // create a map of state names with total count of postal codes in each state
        Map<String, Integer> counts = new HashMap<String, Integer>()
        codes.each {
            counts.put(it.state,
                    counts.containsKey(it.state) ? counts.get(it.state) + 1 : 1
            )
        }

        // convert map to a list of state objects
        List<State> states = new LinkedList<State>()
        counts.each { k, v -> states << new State(k, v) }

        // sort states in ascending order based on the state name
        return states.sort { a, b -> a.name <=> b.name }
    }
}
