package postalcode.util

import java.util.concurrent.FutureTask

class GetPostalCodesTest extends GroovyTestCase {

    private static String[][] TEST_STATES = [["MN","Minnesota"], ["TX","Texas"], ["CA", "California"]]

    void testGet() {
        def callable = new GetPostalCodes(10, 5, "llinder", TEST_STATES)
        def future = new FutureTask(callable)

        assertFalse(future.isDone())
        assertFalse(future.isCancelled())

        future.run()
        def result = future.get()
        assertNotNull(result)
        assertEquals(27, result.size())
    }

}
