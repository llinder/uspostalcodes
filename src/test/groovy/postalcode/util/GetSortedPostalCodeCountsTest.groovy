package postalcode.util

import java.util.concurrent.FutureTask

import postalcode.model.State

class GetSortedPostalCodeCountsTest extends GroovyTestCase {

    private static String[][] TEST_STATES = [["MN","Minnesota"], ["TX","Texas"], ["CA", "California"]]

    void testGet() {
        def pcCallable = new GetPostalCodes(10, 5, "llinder", TEST_STATES)
        def pcFuture = new FutureTask(pcCallable)
        pcFuture.run()

        def callable = new GetSortedPostalCodeCounts(pcFuture)
        def future = new FutureTask(callable)

        assertFalse(future.isDone())
        assertFalse(future.isCancelled())

        future.run()
        def result = future.get()
        assertNotNull(result)
        assertEquals(3, result.size())
        assertEquals([
                new State("California", 7),
                new State("Minnesota", 10),
                new State("Texas", 10)
        ], result)
    }

}
