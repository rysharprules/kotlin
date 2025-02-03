# Exception Handling

## Coroutines

```kotlin
import kotlinx.coroutines.*
fun main(): Unit = runBlocking {
    try {
        launch {
            // exception thrown in different coroutine than the one that launched it
            throw UnsupportedOperationException("Ouch!")
        }
    } catch (u: UnsupportedOperationException) {
        println("Handled $u") // Not called
    }
}
// Exception in thread "main" java.lang.UnsupportedOperationException: Ouch!
// at MyExampleKt$main$1$1.invokeSuspend(MyExample.kt:6)
// ...
```
```kotlin
import kotlinx.coroutines.*
fun main(): Unit = runBlocking {
    launch {
        try {
            throw UnsupportedOperationException("Ouch!")
        } catch (u: UnsupportedOperationException) {
            println("Handled $u")
        }
    }
}
// Handled java.lang.UnsupportedOperationException: Ouch!
```

### Error propagation
Coroutines cancel all their children when one child fails. The parent-child hierarchy between coroutines is built via `Job` objects

<img src=../img/core/13/propagation.png width=500 height="220">

```kotlin
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {
    launch {
        try {
            while (true) {
                println("Heartbeat!")
                delay(500.milliseconds)
            }
        } catch (e: Exception) {
            println("Heartbeat terminated: $e")
            throw e
        }
    }
    launch {
        delay(1.seconds)
        throw UnsupportedOperationException("Ow!")
    }
}
// Heartbeat!
// Heartbeat!
// Heartbeat terminated: kotlinx.coroutines.JobCancellationException: Parent job is Cancelling; job=BlockingCoroutine{Cancelling}@1517365b 
// Exception in thread "main" java.lang.UnsupportedOperationException: Ow!
```

#### Supervisors
For a coroutine to act as a supervisor for its children, the job associated with it should be 
a `SupervisorJob` instead of a regular `Job`. It takes the same role but prevents the 
propagation of exceptions to parents and doesn’t trigger the cancellation of other child jobs 
as a response.

```kotlin
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {
    supervisorScope {
        launch {
            try {
                while (true) {
                    println("Heartbeat!")
                    delay(500.milliseconds)
                }
            } catch (e: Exception) {
                println("Heartbeat terminated: $e")
                throw e
            }
        }
        launch {
            delay(1.seconds)
            throw UnsupportedOperationException("Ow!")
        }
    }
}
// Heartbeat!
// Heartbeat!
// Exception in thread "main" java.lang.UnsupportedOperationException: Ow!
// ...
// Heartbeat!
// Heartbeat!
// ...
```

### CoroutineExceptionHandler
A special handler, part of the coroutine context.

If no coroutine exception handler is present in the context, the uncaught exception
moves to the system-wide exception handler.

```kotlin
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

class ComponentWithScope(dispatcher: CoroutineDispatcher = Dispatchers.Default) {
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("[ERROR] ${e.message}")
    }
    private val scope = CoroutineScope(
        SupervisorJob() + dispatcher + exceptionHandler
    )
    fun action() = scope.launch {
        throw UnsupportedOperationException("Ouch!")
    }
}

fun main() = runBlocking {
    val supervisor = ComponentWithScope()
    supervisor.action()
    delay(1.seconds)
}
// [ERROR] Ouch!
```
Handlers installed in the contexts of coroutines that aren’t root coroutines are never used.

### Async/Await

If a coroutine created using `async` throws an exception, calling `await` on its result rethrows this 
exception. That’s because `await` can’t return a meaningful value of the expected type and, therefore, 
needs to throw an exception.
```kotlin
import kotlinx.coroutines.*
fun main(): Unit = runBlocking {
    val myDeferredInt: Deferred<Int> = async {
        throw UnsupportedOperationException("Ouch!")
    }
    try {
        val i: Int = myDeferredInt.await()
        println(i)
    } catch (u: UnsupportedOperationException) {
        println("Handled: $u")
    }
}
// Handled: java.lang.UnsupportedOperationException: Ouch!
// Exception in thread "main" java.lang.UnsupportedOperationException: Ouch!
// at MyExampleKt$main$1$myDeferred$1.invokeSuspend(MyExample.kt:6)
//...
```

Having an `async` as the topmost coroutine means a `CoroutineExceptionHandler` is not invoked.

When the topmost coroutine is started using `async`, it becomes the responsibility of the 
consumer of the `Deferred` to handle this exception by calling `await()` — the coroutine 
exception handler can ignore this exception, and the calling code, which works with the
`Deferred`, can wrap its `await` invocation in a try-catch block.

