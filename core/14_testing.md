# Testing

## Coroutines and flows

### `advanceUntilIdle`
When testing coroutines, especially those that use delays or timers, you often need to control the flow of time. By 
using `advanceUntilIdle`, you can simulate the passage of time without actually waiting for real time to pass. This is 
helpful in tests that involve suspending functions with delays or scheduled jobs.

- It advances the virtual time until all tasks that are scheduled (e.g., delays, time-based operations) have been 
executed and the system becomes "idle."
- It allows you to bypass waiting for real-world time and ensures that asynchronous tasks that rely on delayed or 
scheduled operations are tested in a controlled and predictable manner.

```kotlin
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals

class CoroutineTest {

    @Test
    fun testAdvanceUntilIdle() = runTest {
        var result = 0

        // Start a coroutine that performs a delay
        launch {
            delay(1000)  // Simulating a delay
            result = 42
        }

        // Advance the virtual time until all coroutines are idle
        advanceUntilIdle()

        // Now, the result should be updated because the delay was completed
        assertEquals(42, result)
    }
}
```

### `runCurrent`
`runCurrent` specifically runs all the tasks that are currently scheduled but haven't executed yet, advancing the virtual 
time enough for them to be completed, without advancing time beyond the current task.

- It runs any tasks that are scheduled but haven't yet been executed (for example, delayed coroutines).
- It helps you control the flow of time in a coroutine test by executing just those tasks that are ready to run at the moment.
- It is more granular than `advanceUntilIdle()`, which would run everything that is scheduled until all tasks are done.

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CoroutineTest {

    @Test
    fun testRunCurrent() = runTest {
        var result = 0

        // Launch a coroutine that does something after a delay
        launch {
            delay(1000)  // Simulate a delay
            result = 42
        }

        // At this point, the task is scheduled but hasn't run yet
        // Using runCurrent will run any tasks that are scheduled but haven't executed
        runCurrent()  // This advances the virtual time just enough to run the delayed task

        // The result should now be updated because the delayed task was executed
        assertEquals(42, result)
    }
}
```

### `runTest`
Used to run code within a coroutine-based test environment. It allows you to write tests for coroutines and suspend 
functions in a structured manner.

- It creates a test coroutine scope to run your test code inside.
- It provides functionality for running asynchronous tests synchronously, making it easier to test coroutines.
- It also helps you control timing, like advancing time in the case of delayed operations or testing timed functions.

```kotlin
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals

class MyCoroutineTest {

    @Test
    fun testCoroutineFunction() = runTest {
        val result = mySuspendingFunction()
        assertEquals("Hello, world!", result)
    }

    suspend fun mySuspendingFunction(): String {
        delay(1000)  // Simulate a delay
        return "Hello, world!"
    }
}
```