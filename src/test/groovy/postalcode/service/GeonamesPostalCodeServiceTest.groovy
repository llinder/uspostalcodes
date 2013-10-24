package postalcode.service

import postalcode.model.State

class GeonamesPostalCodeServiceTest extends GroovyTestCase {

    private static String[][] TEST_STATES = [
            ["MN","Minnesota"], ["TX","Texas"], ["CA", "California"],
            ["WI","Wisconsin"], ["OR", "Oregon"], ["NV", "Nevada"]]

    private final PostalCodeService service = new GeonamesPostalCodeService(10, 5, "llinder", TEST_STATES)

    void testGetSortedPostalCodeCounts() {
        def future = service.getSortedPostalCodeCounts()

        assertNotNull(future)
        def result = future.get()
        assertNotNull(result)
        assertEquals(6, result.size())
        assertEquals([
                new State("California", 7),
                new State("Minnesota", 10),
                new State("Nevada", 7),
                new State("Oregon", 5),
                new State("Texas", 10),
                new State("Wisconsin", 10)
        ], result)

    }
}
