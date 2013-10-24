package postalcode.service

import java.util.concurrent.*

import postalcode.model.State

/**
 * Postal code service interface
 */
interface PostalCodeService {

    /**
     * Returns a future list of State objects containing
     * postal code counts for each state. The state objects
     * are returned in sorted ascending order.
     * @return
     */
    Future<List<State>> getSortedPostalCodeCounts()

    /**
     * Clears any cached results
     */
    void clear()

}

