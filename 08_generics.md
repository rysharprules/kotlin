# Generics

## Syntax

<img src=img/08_syntax.png width=390 height=100>

```kotlin
fun main() {
    val letters = ('a'..'z').toList()
    
    // Specifies the type argument explicitly
    println(letters.slice<Char>(0..2)) // [a, b, c]
    
    // The compiler infers that T is Char here.
    println(letters.slice(10..13)) // [k, l, m, n]
}
```
```kotlin
val <T> List<T>.penultimate: T 
    get() = this[size - 2]

fun main() {
    println(listOf(1, 2, 3, 4).penultimate) // 3
}
```
Interfaces and classes use angle brackets
```kotlin
interface Comparable<T> {
    fun compareTo(other: T): Int
}

class String : Comparable<String> {
    override fun compareTo(other: String): Int = TODO()
}
```

## Type constraints

In Java, you use the keyword extends to express the same concept:
```java
<T extends Number> T sum(List<T> list)
```
<img src=img/08_constraints.png width=390 height=100>

In this case, the `sum` function is constrained to lists of a type whose upper bound is `Number`.

```kotlin
fun main() {
    println(listOf(1, 2, 3).sum()) // 6
}
```

## Nullability
A type parameter with no upper bound specified will have the upper bound of `Any?`
```kotlin
class Processor<T> {
    fun process(value: T) {
        value?.hashCode() // value is nullable, so you have to use a safe call.
    }
}

// String?, which is a nullable type, is substituted for T.
val nullableStringProcessor = Processor<String?>() 
// This code compiles fine, having null as the value argument.
nullableStringProcessor.process(null)
```

If you want to guarantee that a non-null type will always be substituted for a type parameter, 
you can achieve this by specifying a constraint. 

If you don’t have any restrictions other than nullability, you can use `Any` as the upper
bound, replacing the default `Any?`:

```kotlin
class Processor<T : Any> { // Specifying a non-null upper bound
    fun process(value: T) {
        value.hashCode() // The value of type T is now non-null.
    }
}

val nullableStringProcessor = Processor<String?>()
// Error: Type argument is not within its bounds: should be subtype of 'Any'
```

## Star projections
Analog of Java’s `List<?>`
```kotlin
if (value is List<*>) { /* ... */ }
```
```kotlin
fun printSum(c: Collection<*>) {
    // Warning here: Unchecked cast: List<*> to List<Int>
    val intList = c as? List<Int> ?: throw IllegalArgumentException("List is expected")
    println(intList.sum())
}

fun main() {
    // With lists, everything works as expected.
    printSum(listOf(1, 2, 3)) // 6
    
    // The set isn’t a list, so an exception is thrown.
    printSum(setOf(1, 2, 3)) // IllegalArgumentException: List is expected

    // The cast succeeds, but since strings cannot be summed, another exception is thrown later.
    printSum(listOf("a", "b", "c")) // ClassCastException: String cannot be cast to Number
}
```
`as` and `as?` casts won’t fail if the class has the correct base type and an incorrect type
argument because the type argument isn’t known at run time when the cast is performed. 
Because of that, the compiler will emit an “unchecked cast” warning.

## `reified` type parameters
Type parameters of inline functions can be reified, which means you can refer to actual 
type arguments at run time.

```kotlin
fun <T> isA(value: Any) = value is T
// Error: Cannot check for instance of erased type: T
```
```kotlin
inline fun <reified T> isA(value: Any) = value is T // Now, this code compiles.

fun main() {
    println(isA<String>("abc")) // true
    println(isA<String>(123)) // false
}
```
`reified` declares that this type parameter will not be erased at run time.
```kotlin
inline fun <reified T> Iterable<*>.filterIsInstance(): List<T> {
    val destination = mutableListOf<T>()
    for (element in this) {
        // You can check whether the element is an instance of the class specified as a type argument.
        if (element is T) {
        // if (element is String) { // the generated code will be equivalent to this, a reference to a specific class
            destination.add(element)
        }
    }
    return destination
}
```
If a property accessor is defined on a generic type, marking the property as inline and the type parameter as reified allows you to 
reference the specific class used as the type argument.
```kotlin
inline val <reified T> T.canonical: String
    get() = T::class.java.canonicalName

fun main() {
    println(listOf(1, 2, 3).canonical) // java.util.List
    println(1.canonical) // java.lang.Integer
}
```
How you can use a reified type parameter:

- In type checks and casts (`is`, `!is`, `as`, `as?`)
- To use the Kotlin reflection APIs (`::class`)
- To get the corresponding `java.lang.Class` (`::class.java`)
- As a type argument to call other functions

You can’t do the following:

- Create new instances of the class specified as a type parameter
- Call methods on the companion object of the type parameter class
- Use a non-reified type parameter as a type argument when calling a function with a reified type parameter
- Mark type parameters of classes or non-inline functions as `reified`

## Variance
Variance refers to how subtyping relationships between generic types are propagated.

Covariants use the `in` position and contravariants use the `out` positions:

<img src=img/08_variance.png width=300 height=140>

<img src=img/08_variance2.png width=460 height=140>

### Subtypes
<img src=img/08_subtypes.png width=400 height=100> 

`B` is a subtype of `A` if you can use it when `A` is expected. Since you can use an `Int` where a `Number` is expected, 
it is a subtype. Likewise, you can use an `Int` where an `Int` is expected; it is also a subtype of itself. Because you 
can’t use an `Int` where a `String` is expected, it can’t be considered a subtype.

<img src=img/08_null_subtypes.png width=300 height=100>

A non-null type `A` is a subtype of nullable `A?`, but not vice versa: you can use an `Int` where an `Int?` is expected, but you
can’t use an `Int?` where an `Int` is expected.

### Covariance `out`
Covariance allows you to specify that a type can only be used in a producer (output) position, meaning you can safely use 
a type as a subtype of another.

`out` allows you to use a type in a read-only manner. This means that you can only get items from a collection but not modify them.
```kotlin
// Covariant generic type, only allows reading elements of type T
class Box<out T>(val value: T)

fun printBox(box: Box<out Number>) {
    val number = box.value  // We can read but not modify the value.
    println(number)
}

fun main() {
    val intBox = Box(42)  // Box<Int>
    printBox(intBox)       // Works because Box<Int> is a subtype of Box<out Number>
}
```
`List` is a read-only interface that defines only methods that return `T` (so `T` is in the `out` position)
```kotlin
interface List<out T> : Collection<T> {
    operator fun get(index: Int): T
    fun subList(fromIndex: Int, toIndex: Int): List<T>
}
```

### Contravariance `in`
Contravariance allows you to specify that a type can only be used in a consumer (input) position, meaning you can safely 
use a type as a supertype of another.

The concept of contravariance can be thought of as a mirror to covariance: for a contravariant class, the 
subtyping relation is the opposite of the subtyping relations of classes used as its type arguments.

`in` allows you to use a type in a write-only manner. This means that you can add items to a collection but not read from it.
```kotlin
// Contravariant generic type, only allows writing elements of type T
class Consumer<in T> {
    fun consume(value: T) {
        println("Consuming: $value")
    }
}

fun processNumbers(consumer: Consumer<in Number>) {
    consumer.consume(42)   // Works because Int is a subtype of Number
    consumer.consume(3.14) // Works because Double is a subtype of Number
}

fun main() {
    val intConsumer = Consumer<Number>()
    processNumbers(intConsumer)
}
```
The `Comparator` interface only consumes values of type `T`. That means `T` is used only in `in` positions; 
therefore, its declaration can be preceded by the `in` keyword.
```kotlin
interface Comparator<in T> {
    fun compare(e1: T, e2: T): Int { /* ... */ } 
}
```

### Invariant (no variance modifier)
If you don't specify a variance modifier, the generic type is invariant. This means the type parameter can only be exactly 
the specified type, not any subtypes or supertypes.
```kotlin
class Box<T>(val value: T)

fun printBox(box: Box<Number>) {
    val number = box.value
    println(number)
}

fun main() {
    val numberBox: Box<Number> = Box(42)    // Box<Number>
    printBox(numberBox)

    // The following will cause a compile-time error:
    // val intBox = Box(42) // Box<Int>
    // printBox(intBox)           // Cannot pass Box<Int> to Box<Number>
}
```

### Summary of variance

| **Covariant**            | **Contravariant**          | **Invariant**            |
|--------------------------|----------------------------|--------------------------|
| `Producer<out T>`         | `Consumer<in T>`           | `MutableList<T>`         |
| Subtyping for the class is preserved: `Producer<Cat>` is a subtype of `Producer<Animal>`. | Subtyping is reversed: `Consumer<Animal>` is a subtype of `Consumer<Cat>`. | No subtyping.            |
| `T` only in `out` positions | `T` only in `in` positions  | `T` in any position      |

### Use-site variance

Refers to a concept that allows you to specify the variance of a generic type at the _usage site_ (when the 
type is used in a particular context), rather than when the type is declared.

```kotlin
fun <T> copyData(source: MutableList<T>,
                 // Allows the destination element type to be a supertype of the source element type                 
                 destination: MutableList<in T>) { 
    for (item in source) {
        destination.add(item)
    }
}
```

## Type aliases
Can improve code readability and simplify complex or repetitive type declarations.
```kotlin
// A type alias is defined using the typealias keyword, the alias, and the underlying type.
typealias NameCombiner = (String, String, String, String) -> String 
// Type aliases can be used wherever you would’ve used the underlying type, like variable declarations
val authorsCombiner: NameCombiner = { a, b, c, d -> "$a et al." }  
val bandCombiner: NameCombiner = { a, b, c, d -> "$a, $b & The Gang" } 

// ...or function parameter declarations.
fun combineAuthors(combiner: NameCombiner) {
 println(combiner("Sveta", "Seb", "Dima", "Roman"))
}

fun main() {
    // The type alias resolves to the underlying type. So it’s perfectly fine to pass a NameCombiner
    combineAuthors(bandCombiner) // Sveta, Seb & The Gang
    combineAuthors(authorsCombiner) // Sveta et al.
    // ...or a lambda taking four strings and returning a single string.
    combineAuthors { a, b, c, d -> "$d, $c & Co."}  // Roman, Dima & Co.
}
```