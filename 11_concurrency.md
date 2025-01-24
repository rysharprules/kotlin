# Concurrency
- [Project Loom](https://wiki.openjdk.org/display/loom) is an effort to bring
  lightweight concurrency in the form of virtual threads natively to the JVM
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) library developed by JetBrains.
- `-Dkotlinx.coroutines.debug` JVM option - you additionally get information about the coroutine name next to the thread name
  to the thread name

## Coroutines
Coroutines are a lightweight abstraction on top of threads. Coroutines are a powerful feature
of Kotlin, providing an elegant way to write concurrent code that can run asynchronously and
that is non-blocking.

Where a single process may have thousands of threads, you can run millions of concurrent tasks using coroutines.

<img src=img/11_coroutines.png width=600 height=210>

### Pausing functions with `suspend`

- Indicates that this function may pause execution.
- Trying to call a suspending function from regular, non-suspending code, will result in an error

```kotlin
suspend fun login(credentials: Credentials): UserID
suspend fun loadUserData(userID: UserID): UserData
fun showData(data: UserData) 
suspend fun showUserInfo(credentials: Credentials) { 
    val userID = login(credentials)
    val userData = loadUserData(userID)
    showData(userData)
}
```
Using suspending functions means that waiting functions don’t block execution. They’re suspended
instead and make way for any other functions that might want to run in the meantime.
<img src=img/11_suspend.png width=600 height=200>

### Building coroutines with `runBlocking`, `launch`, `async`

- `runBlocking` is designed for bridging the world of blocking code and suspending functions.
- `launch` is used for starting new coroutines that don’t return any values.
- `async` is for computing values in an asynchronous manner.

| Builder      | Return value             | Used for                                               |
|--------------|--------------------------|--------------------------------------------------------|
| `runBlocking`  | Value calculated by lambda | Bridging blocking and nonblocking code                |
| `launch`       | `Job`                      | Start-and-forget tasks (that have side effects)       |
| `async`        | `Deferred<T>`              | Calculating a value asynchronously (which can be awaited) |


```kotlin
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

suspend fun doSomethingSlowly() {
  delay(500.milliseconds) 
  println("I'm done")
}
fun main() = runBlocking {
    doSomethingSlowly()
}
```
Within the `runBlocking` coroutine, you’re free to start any number of additional child coroutines with `launch`.

`launch` returns an object of type `Job`.
```kotlin
fun main() = runBlocking {
  log("The first, parent, coroutine starts")
  launch {
    log("The second coroutine starts and is ready to be suspended")
    delay(100.milliseconds) // suspension point
    log("The second coroutine is resumed")
  }
  launch {
    log("The third coroutine can run in the meantime")
  }
  log("The first coroutine has launched two more coroutines")
}
// 36 [main @coroutine#1] The first, parent, coroutine starts 
// 40 [main @coroutine#1] The first coroutine has launched two more coroutines
// 42 [main @coroutine#2] The second coroutine starts and is ready to be suspended
// 47 [main @coroutine#3] The third coroutine can run in the meantime
// 149 [main @coroutine#2] The second coroutine is resumed
```
<img src=img/11_launch.png width=600 height=200>

```kotlin
suspend fun slowlyAddNumbers(a: Int, b: Int): Int {
  log("Waiting a bit before calculating $a + $b")
  delay(100.milliseconds * a)
  return a + b
}
fun main() = runBlocking {
  log("Starting the async computation")
  // Starts a new coroutine for each async call
  val myFirstDeferred = async { slowlyAddNumbers(2, 2) }
  val mySecondDeferred = async { slowlyAddNumbers(4, 4) }
  log("Waiting for the deferred value to be available")
  // Waits for the results to be available
  log("The first result: ${myFirstDeferred.await()}")
  log("The second result: ${mySecondDeferred.await()}")
}
// 0 [main @coroutine#1] Starting the async computation 
// 4 [main @coroutine#1] Waiting for the deferred value to be available
// 8 [main @coroutine#2] Waiting a bit before calculating 2 + 2
// 9 [main @coroutine#3] Waiting a bit before calculating 4 + 4
// 213 [main @coroutine#1] The first result: 4
// 415 [main @coroutine#1] The second result: 8
```
Even though the whole application runs on a single thread, using `async`, you can compute 
multiple values concurrently.
<img src=img/11_async.png width=600 height=500>

### Dispatchers
The dispatcher for a coroutine determines what thread(s) the coroutine uses for
its execution. By choosing a dispatcher, you can confine the execution of a
coroutine to a specific thread or dispatch it to a thread pool.

Coroutines inherit their dispatcher from their parent by default.

| Dispatcher            | Number of threads                                   | Used for                                                       |
|-----------------------|------------------------------------------------------|--------------------------------------------------------------|
| `Dispatchers.Default`    | Number of CPU cores                                 | General-purpose operations, CPU-bound operations              |
| `Dispatchers.Main`       | One UI-bound logic (“UI thread”), only when in the context of a UI framework | UI-related tasks                                               |
| `Dispatchers.IO`         | Up to 64 threads (auto-scaling based on blocking IO or number of CPU cores, whichever is larger) | Offloading blocking IO tasks                                  |
| `Dispatchers.Unconfined` | - ("Whatever thread")                               | Advanced cases where immediate scheduling is required (non-general-purpose) |
| `limitedParallelism(n)`  | Custom (n)                                          | Custom scenarios                                                |

<img src=img/11_dispatcher.png width=600 height=400>

```kotlin
fun main() {
  runBlocking {
    log("Doing some work")
    launch(Dispatchers.Default) { 
        log("Doing some background work")
    }
  }
}
// 26 [main @coroutine#1] Doing some work
// 33 [DefaultDispatcher-worker-1 @coroutine#2] Doing some background work
```
To switch dispatchers for an already existing coroutine, you can use the `withContext` function and pass a different dispatcher
```kotlin
launch(Dispatchers.Default) { 
    val result = performBackgroundOperation()
    withContext(Dispatchers.Main) { updateUI(result) }
}
```
The call to `withContext` causes the coroutine, which was originally started on the default dispatcher and running on one of its worker
threads, to execute on the specified dispatcher.
<img src=img/11_withcontext.png width=600 height=170>

### Scope

```kotlin
fun main() {
  runBlocking { // this: CoroutineScope 
    // The coroutine started by launch is a child of the parent runBlocking coroutine
    launch { // this: CoroutineScope 
      delay(1.seconds)
      launch {
        delay(250.milliseconds)
        log("Grandchild done")
      }
      log("Child 1 done!")
    }
    launch {
      delay(500.milliseconds)
      log("Child 2 done!")
    }
    log("Parent done!")
  }
}
// 29 [main @coroutine#1] Parent done!
// 539 [main @coroutine#3] Child 2 done!
// 1039 [main @coroutine#2] Child 1 done!
// 1293 [main @coroutine#4] Grandchild done
// the program doesn’t actually terminate until all child coroutines have completed
```
Coroutines exist in a hierarchy. Each coroutine knows its children or parents. `runBlocking` can wait for 
the completion of all its children before returning.

<img src=img/11_scope.png width=500 height=300>

```kotlin
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

suspend fun generateValue(): Int { 
    delay(500.milliseconds)
    return Random.nextInt(0, 10)
}
// computeSum is suspending.
suspend fun computeSum() { 
  log("Computing a sum...")
  // The coroutineScope function provides you with a scope.
  val sum = coroutineScope {  
    val a = async { generateValue() }
    val b = async { generateValue() } 
    // Before returning, coroutineScope waits for all child coroutines to finish
    a.await() + b.await()  
  }
  log("Sum is $sum")
}
fun main() = runBlocking {
    computeSum()
}
// 0 [main @coroutine#1] Computing a sum...
// 532 [main @coroutine#1] Sum is 10
```
`coroutineScope` is used for the concurrent decomposition of work: leveraging multiple coroutines together to perform a computation
<img src=img/11_scope2.png width=500 height=290>

### Cancellation
`launch` coroutine builder returns a `Job`, and the `async` coroutine builder returns a `Deferred`. 
Both of them allow you to call `cancel`.

The `withTimeout` and `withTimeoutOrNull` functions allow you to compute a value while constraining the maximum amount of 
time spent for the computation.

`withTimeout` function throws a `TimeoutCancellationException` in case the timeout was exceeded.

```kotlin
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

suspend fun calculateSomething(): Int {
  delay(3.seconds)
  return 2 + 2
}
fun main() = runBlocking {
    val quickResult = withTimeoutOrNull(500.milliseconds) {
        calculateSomething()
    }
    println(quickResult) // null
  
    val slowResult = withTimeoutOrNull(5.seconds) {
        calculateSomething()
    }
    println(slowResult) // 4
}
```
Using `withTimoutOrNull` constrains the execution time of a suspending function. When the function 
returns a value within the given timeout, that value is returned immediately. When the timeout expires, 
the function is cancelled, and `null` is returned.

<img src=img/11_cancel.png width=500 height=290>

Triggering the cancellation of the outermost launched coroutine properly cancels even the great-grandchild coroutine:
```kotlin
fun main() = runBlocking {
  val job = launch {
    launch {
      launch {
        launch { // This coroutine is a great-grandchild of the job that’s cancelled.
          log("I'm started")
          delay(500.milliseconds)
          log("I'm done!")
        }
      }
    }
  }
  delay(200.milliseconds)
  job.cancel()
}
// 0 [main @coroutine#5] I'm started
```

You can check cancellation status with the `Boolean` `isActive` property of a `CoroutineScope`.
```kotlin
val myJob = launch {
  repeat(5) {
    doCpuHeavyWork()
    if(!isActive) return@launch
  }
}
```
Alternatively you can use `ensureActive`. If the coroutine is no longer active, the `ensureActive` function throws a `CancellationException`
```kotlin
val myJob = launch {
  repeat(5) {
    doCpuHeavyWork()
    ensureActive()
  }
}
```

### `yield`
A suspending function that introduces a point in your code where a `CancellationException` can be thrown, and also
allows the dispatcher to switch to working on a different coroutine if there is one waiting.
```kotlin
import kotlinx.coroutines.*

suspend fun doCpuHeavyWork(): Int {
  var counter = 0
  val startTime = System.currentTimeMillis()
  while (System.currentTimeMillis() < startTime + 500) {
    counter++
    yield()
  }
  return counter
}

fun main() {
  runBlocking {
    launch {
      repeat(3) {
        doCpuHeavyWork()
      }
    }
    launch {
      repeat(3) {
        doCpuHeavyWork()
      }
    }
  }
}

// 0 [main @coroutine#2] I'm doing work!
// 559 [main @coroutine#3] I'm doing work!
// 1062 [main @coroutine#2] I'm doing work!
// 1634 [main @coroutine#3] I'm doing work!
// 2208 [main @coroutine#2] I'm doing work!
// 2734 [main @coroutine#3] I'm doing work!

// without yield() (a suspension point) in the body of the coroutine, there is never an opportunity for 
// the underlying coroutines machinery to pause the execution of the first coroutine and start the 
// execution of the second coroutine which would result in:
// 29 [main @coroutine#2] I'm doing work!
// 533 [main @coroutine#2] I'm doing work!
// 1036 [main @coroutine#2] I'm doing work!
// 1537 [main @coroutine#3] I'm doing work!
// 2042 [main @coroutine#3] I'm doing work!
// 2543 [main @coroutine#3] I'm doing work!

```

| Function/Property | Use case                                                                 |
|-------------------|--------------------------------------------------------------------------|
| isActive          | Checks to see if cancellation was requested (to do some finishing work before stopping work) |
| ensureActive      | Introduces “cancellation point”—throws `CancellationException` upon cancellation, instantly stopping work |
| yield()           | Relinquishes computation resources, preventing CPU-heavy computations from exhausting the underlying thread (pool) |

<img src=img/11_yield.png width=600 height=210>

Without suspension points, multiple coroutines will always run to completion and (on a single-threaded 
dispatcher) without interleaving. Checking isActive or calling ensureActive allows the coroutines to
cancel their work prematurely at these cancellation points. Using yield to let other coroutines use the 
underlying thread means the coroutines can run interleaved.

### `Mutex`
A `Mutex` is a mutual exclusion lock that allows only one coroutine to access a critical section of code at a time.
```kotlin
fun main() {
  runBlocking {
    var x = 0
    repeat(10_000) {
        launch(Dispatchers.Default) { x++ }
    }
    delay(1.seconds)
    println(x) // 9,916
  }
}
```

```kotlin
fun main() = runBlocking {
  val mutex = Mutex()
  var x = 0
  repeat(10_000) {
    launch(Dispatchers.Default) {
      mutex.withLock {
          x++
      }
    }
  }
  delay(1.seconds)
  println(x) // 10000
}
```