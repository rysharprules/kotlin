# Flows

Flows allow you to work with elements as they are emitted. 

```kotlin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

fun createValues(): Flow<Int> {
    return flow {
        // Each emission is immediately passed to the collector.
        emit(1) 
        delay(1000.milliseconds)
        emit(2)
        delay(1000.milliseconds)
        emit(3)
        delay(1000.milliseconds)
    }
}
fun main() = runBlocking {
    val myFlowOfValues = createValues()
    // The values are printed as soon as they are emitted.
    myFlowOfValues.collect { log(it) } 
}
// 29 [main @coroutine#1] 1
// 1100 [main @coroutine#1] 2
// 2156 [main @coroutine#1] 3
```

There are two categories of flows:

- **Cold flows** represent asynchronous data streams that only start emitting items when 
their items are being consumed by an individual collector.
- **Hot flows**, on the other hand, produce items independently of whether the items are 
actually being consumed, operating in a broadcast fashion.

| Aspect                                       | Cold Flow                            | Hot Flow                               |
|----------------------------------------------|--------------------------------------|----------------------------------------|
| Default Behavior                             | Inert (triggered by the collector)   | Active by default                      |
| Number of Subscribers                        | Has a collector                      | Has multiple subscribers               |
| Emission Distribution                        | Collector gets all emissions         | Subscribers get emissions from the start of subscription |
| Completion Status                            | Potentially completes                | Doesn’t complete                       |
| Emission Source                              | Emissions happen from a single coroutine (unless channelFlow is used) | Emissions can happen from arbitrary coroutines |

Intermediate and terminal operators can transform flows:

<img src=../img/core/12/operators.png width=600 height=300>

## Cold flows

This flow is initially inert. It’s not actually executed until a terminal operator is invoked 
on the flow that kicks off the computation as defined in the builder.
```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

fun main() {
    val letters = flow {
        log("Emitting A!")
        emit("A") // emit offers a value to the collector of the flow.
        delay(200.milliseconds)
        log("Emitting B!")
        emit("B")
    }
}
// No output. The builder function returns you an object of type Flow<T>
```

Calling the collect function on a Flow runs its logic, with the code responsible for 
collecting the flow typically referred to as the collector.
```kotlin
val letters = flow {
    log("Emitting A!")
    emit("A")
    delay(200.milliseconds)
    log("Emitting B!")
    emit("B")
}
fun main() = runBlocking {
    letters.collect {
        log("Collecting $it")
        delay(500.milliseconds)
    }
}
// 27 [main @coroutine#1] Emitting A!
// 38 [main @coroutine#1] Collecting A
// 757 [main @coroutine#1] Emitting B!
// 757 [main @coroutine#1] Collecting B
```
The first emission in the flow builder means the lambda associated with the collector is invoked.
These operations all run on the same coroutine.

<img src=../img/core/12/cold.png width=400 height=350>

Calling collect on a cold flow multiple times also triggers the execution of its code multiple times.
```kotlin
fun main() = runBlocking {
    letters.collect {
        log("(1) Collecting $it")
        delay(500.milliseconds)
    }
    letters.collect {
        log("(2) Collecting $it")
        delay(500.milliseconds)
    }
}
// 23 [main @coroutine#1] Emitting A!
// 33 [main @coroutine#1] (1) Collecting A
// 761 [main @coroutine#1] Emitting B!
// 762 [main @coroutine#1] (1) Collecting B
// 1335 [main @coroutine#1] Emitting A!
// 1335 [main @coroutine#1] (2) Collecting A
// 2096 [main @coroutine#1] Emitting B!
// 2096 [main @coroutine#1] (2) Collecting B
```

### Cancelling

By cancelling the coroutine of the collector, you stop the collection of the flow at the next cancellation point.
`emit` acts as a cancellation and suspension point.
```kotlin
val counterFlow = flow {
    var x = 0
    while (true) {
        emit(x++)
        delay(200.milliseconds)
    }
}
fun main() = runBlocking {
    val collector = launch {
        counterFlow.collect {
            println(it)
        }
    }
    delay(5.seconds)
    collector.cancel()
}
// 1 2 3 ... 24
```

### Channel Flows

```kotlin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

suspend fun getRandomNumber(): Int {
    delay(500.milliseconds)
    return Random.nextInt()
}
val randomNumbers = flow {
    repeat(10) {
        emit(getRandomNumber())
    }
}
fun main() = runBlocking {
        randomNumbers.collect {
        log(it)
    }
}
// 583 [main @coroutine#1] 1514439879
// 1120 [main @coroutine#1] 1785211458
// 1693 [main @coroutine#1] -996479986
// ...
// 5463 [main @coroutine#1] -2047597449
```
Collecting this flow takes a little over 5 seconds because each `getRandomNumber` invocation is executed one after the other - entirely sequentially.

<img src=../img/core/12/channel1.png width=640 height=100>

```kotlin
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

// The channel flow can launch other coroutines and send elements concurrently
val randomNumbers = channelFlow { // Creates a new channel flow
    repeat(10) {
        launch {
            send(getRandomNumber()) // send can be called from different coroutines
        }
    }
}
// 553 [main] -1927966915
// 568 [main] 222582016
// ...
// 569 [main] 1827898086
```

Now using a channel flow, `getRandomNumber` gets executed concurrently.

<img src=../img/core/12/channel2.png width=400 height=290>

## Hot flows

Two hot flow implementations:

- **Shared flows**, which are used for broadcasting values
- **State flows**, for the special case of communicating state

### Shared flows

Shared flows operate in a broadcast fashion—independently of whether a subscriber (a collector of a shared flow) is present.

<img src=../img/core/12/shared.png width=400 height=290>

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.*
import kotlin.time.Duration.Companion.milliseconds

class RadioStation {
    // Defines a new mutable shared flow as a private property
    private val _messageFlow = MutableSharedFlow<Int>()  
    // Provides a read-only view of the shared flow
    val messageFlow = _messageFlow.asSharedFlow()
    
    fun beginBroadcasting(scope: CoroutineScope) {
        scope.launch {
            while(true) {
                delay(500.milliseconds)
                val number = Random.nextInt(0..10)
                log("Emitting $number!")
                // Emits a value to the mutable shared flow from a coroutine
                _messageFlow.emit(number)  
            }
        }
    }
}

fun main(): Unit = runBlocking {
    val radioStation = RadioStation()
    radioStation.beginBroadcasting(this)
    delay(600.milliseconds)
    radioStation.messageFlow.collect {
        log("A collecting $it!")
    }
}
// the first value, which was emitted after roughly 500 milliseconds, was not collected by the subscriber
// 611 [main @coroutine#2] Emitting 8!
// 1129 [main @coroutine#2] Emitting 9!
// 1131 [main @coroutine#1] A collecting 9!
// 1647 [main @coroutine#2] Emitting 1!
// 1647 [main @coroutine#1] A collecting 1!
```

There can be multiple collectors.

#### Cold flow to shared flow

You can convert a given cold flow to a hot shared flow by using the `shareIn` function.
This function needs to run on a coroutine so `shareIn` takes a parameter of type `CoroutineScope`.

```kotlin
fun celsiusToFahrenheit(celsius: Int) = celsius * 9.0 / 5.0 + 32.0
fun querySensor(): Int = Random.nextInt(-10..30)
fun getTemperatures(): Flow<Int> {
    return flow {
        while(true) {
            emit(querySensor())
            delay(500.milliseconds)
        }
    }
}

fun main() {
    val temps = getTemperatures()
    runBlocking {
        // This function needs to run on a coroutine so shareIn takes a parameter of type CoroutineScope
        val sharedTemps = temps.shareIn(this, SharingStarted.Lazily)
        launch {
            sharedTemps.collect {
                log("$it Celsius")
            }
        }
        launch {
            sharedTemps.collect {
                log("${celsiusToFahrenheit(it)} Fahrenheit")
            }
        }
    }
}
// 45 [main @coroutine#3] -10 Celsius
// 52 [main @coroutine#4] 14.0 Fahrenheit
// 599 [main @coroutine#3] 11 Celsius
// 599 [main @coroutine#4] 51.8 Fahrenheit
```

`CoroutineScope` parameter starting strategies:
- **Eagerly** starts the collection of the flow immediately
- **Lazily** starts the collection only when the first subscriber appears
- **WhileSubscribed** starts the collection only when the first subscriber appears, and then
cancels the collection of the flow when the last subscriber disappears

### State flows
Only emit values to the collectors when the value has actually changed.

```kotlin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

class ViewCounter {
    // Creates a mutable state flow with an initial value of 0
    private val _counter = MutableStateFlow(0) 
    val counter = _counter.asStateFlow()
    fun increment() {
        // increment (++) operations aren’t atomic, so prefer over _counter.value++
        _counter.update { it + 1 }
    }
}

fun main() {
    val vc = ViewCounter()
    vc.increment() 
    println(vc.counter.value) // 1
}
```

```kotlin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

enum class Direction { LEFT, RIGHT }
class DirectionSelector {
    private val _direction = MutableStateFlow(Direction.LEFT)
    val direction = _direction.asStateFlow()
    fun turn(d: Direction) {
        _direction.update { d }
    }
}

fun main() = runBlocking {
    val switch = DirectionSelector()
    launch {
        switch.direction.collect {
            log("Direction now $it")
        }
    }
    delay(200.milliseconds)
    switch.turn(Direction.RIGHT)
    delay(200.milliseconds)
    switch.turn(Direction.LEFT)
    delay(200.milliseconds)
    switch.turn(Direction.LEFT)
}
// 37 [main @coroutine#2] Direction now LEFT
// 240 [main @coroutine#2] Direction now RIGHT
// 445 [main @coroutine#2] Direction now LEFT
```

<img src=../img/core/12/state.png width=400 height=400>

State flows perform _equality-based conflation_. When the value is assigned the same value as is already set, no new elements are emitted.

#### Cold flow to state flow

You can convert a given cold flow to a hot shared flow by using the `stateIn` function.

```kotlin
fun main() {
    val temps = getTemperatures()
    runBlocking {
        val tempState = temps.stateIn(this)
        println(tempState.value)
        delay(800.milliseconds)
        println(tempState.value)
        // 18
        // -1
    }
}
```