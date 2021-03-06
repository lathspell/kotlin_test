import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Starts to coroutine worker in parallel.
 *
 * delay() is like Thread.sleep() except that it does not block the thread and therefore other coroutines can continue to run.
 *
 * <pre>
 * 2020-12-30T17:16:00.474173900  [Test worker] start
 * 2020-12-30T17:16:00.743646300  [DefaultDispatcher-worker-1 @coroutine#2] 1.1 <---\
 * 2020-12-30T17:16:00.759267600  [DefaultDispatcher-worker-2 @coroutine#3] 2.1      } same coroutine
 * 2020-12-30T17:16:01.798817400  [DefaultDispatcher-worker-1 @coroutine#2] 1.2 <---/
 * 2020-12-30T17:16:01.798817400  [DefaultDispatcher-worker-2 @coroutine#3] 2.2
 * 2020-12-30T17:16:01.814315300  [Test worker] end
</pre>
 */
class LaunchTest {

    @Test
    fun `coroutines with launch`() {
        myPrintlnLog.clear()

        runBlocking {
            val job1 = launch {
                myPrintln("1a")
                delay(1000L)
                myPrintln("1b")
            }

            val job2 = launch {
                myPrintln("2a")
                delay(1000L)
                myPrintln("2b")
            }

            job1.join()
            job2.join()
        }

        assertEquals(listOf("1a", "2a", "1b", "2b"), myPrintlnLog)
    }

}