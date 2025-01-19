# Reflection
A way to access properties and methods of objects dynamically at run time, without knowing in advance what those properties are.

## Kotlin reflection API

- `KClass` represents a class. You get an instance of KClass by writing `MyClass::class`. 
  - Includes useful
  methods for accessing the contents of a class: `simpleName`, `qualifiedName`, `memberProperties`, etc.
- `KFunction` represents a function. You get an instance of KFunction by writing `::myFunction`
- `KProperty` represents a property. You get an instance of KProperty by writing `MyClass::myProperty`
- `KCallable` is a superinterface for functions and properties. 
  - It declares the `call` method, which allows 
  you to call the corresponding function or the getter of the property

<img src=img/10_hierarchy.png width=600 height=340>

### `KClass`
```kotlin
import kotlin.reflect.full.*

class Person(val name: String, val age: Int)

fun main() {
    val person = Person("Alice", 29)
    val kClass = person::class // Returns an instance of KClass<out Person> 
    println(kClass.simpleName) // Person
    kClass.memberProperties.forEach { println(it.name) } // age // name
}
```

### `KFunction` 
#### Using the `KCallable.call` method
```kotlin
fun foo(x: Int) = println(x)
fun main() {
    // Obtains a reference of type KFunction1<Int, Unit> to foo
    val kFunction = ::foo
    // Calls the function with the argument 42
    kFunction.call(42) // 42
}
```
If you try to call the function with an incorrect number of arguments, it will throw a run-time exception:
`“IllegalArgumentException: Callable expects 1 argument, but 0 were provided.”`

#### Using `invoke`
Rather than `call`, `invoke` prevents you from accidentally passing an incorrect number of arguments to the 
function—the code won’t compile.

Types such as `KFunction1` represent functions with different numbers of parameters. Each type extends 
`KFunction` and adds one additional member `invoke` with the appropriate number of parameters. For example,
`KFunction2` declares `operator fun invoke(p1: P1, p2: P2): R`
```kotlin
import kotlin.reflect.KFunction2

fun sum(x: Int, y: Int) = x + y
fun main() {
    val kFunction: KFunction2<Int, Int, Int> = ::sum
    println(kFunction.invoke(1, 2) + kFunction(3, 4)) // 10
    kFunction(1) // ERROR: No value passed for parameter p2
}
```

### `KProperty`
```kotlin
var counter = 0
fun main() {
    val kProperty = ::counter  // kProperty is a reference to counter of type KMutableProperty0<Int>
    kProperty.setter.call(21) // Calls a setter through reflection, passing 21 as an argument
    println(kProperty.get())  // Obtains a property value by calling get
    // 21
}
```