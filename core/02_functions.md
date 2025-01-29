# Functions
Defining and calling functions

- You can change the name of the class or function you’re importing using the `as` keyword to avoid conflicts
- Local functions are inner functions. They have access to everything in the enclosing function

## Default parameter values
```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ", 
    prefix: String = "", 
    postfix: String = ""  ): String { /* ... */ }
```
You can either invoke the function with all the arguments or omit some of them:
```kotlin
fun main() {
    joinToString(list, ", ", "", "") // 1, 2, 3
    joinToString(list) // 1, 2, 3
    joinToString(list, "; ") // 1; 2; 3
}
```

## Named arguments
You can specify the names of some (or all) of the arguments you pass
```kotlin
joinToString(
    postfix = ".",
    separator = " ",
    collection = collection,
    prefix = " "
)
```
When using the regular call syntax, you have to specify the arguments in the same order as in the function declaration, 
and you can omit only trailing arguments. If you use named arguments, you can omit some arguments from the middle of the 
parameter list and specify only the ones you need, in any order you want

## vararg
Instead of three dots after the type (`...`, Kotlin uses the `vararg` modifier on the parameter.
```kotlin
fun listOf<T>(vararg values: T): List<T> { /* implementation */ }

val list = listOf(2, 3, 5, 7, 11)
```

You pass collections and fixed values in one call be Spreading with the spread operator `*`
```kotlin
fun main(args: Array<String>) {
    val list = listOf("args: ", *args)
}
```

## Infix calls and destructuring
Infix calls can be used with functions that have exactly one required parameter.
You need to mark it with the `infix` modifier

When creating Maps we use `to`: `val map = mapOf(1 to "one", 7 to "seven", 53 to "fiftythree")`

Signature of the `mapOf` function
```kotlin
fun <K, V> mapOf(vararg values: Pair<K, V>): Map<K, V>
```

The following two calls are equivalent:
```kotlin
1.to("one") // 1
1 to "one" // 2
```
1. Calls the to function the regular way
1. Calls the to function using an infix notation

Here’s a simplified version of the declaration of the `to` function:
```kotlin
infix fun Any.to(other: Any) = Pair(this, other)
```
In an infix call, the method name is placed immediately between the target object name and the parameter, with no extra
separators.

You can initialize two variables with the contents of a `Pair` directly:
```kotlin
val (number, name) = 1 to "one"
```

This feature is called a destructuring declaration.

<img src=../img/core/02/infix.png width=250 height=240>

You create a pair using the `to` function and unpack it with a destructuring declaration.

## Extensions
An extension function can be called as a member of a class but is defined outside of it.
You cannot override an extension function. They are associated to the type like `static` methods and therefore can only be shadowed.
```kotlin
package strings

fun String.lastChar(): Char = this.get(this.length - 1)
```
- String: Receiver type
- `this` is the Receiver object

When you define an extension function, it doesn’t automatically become available across your entire project. Instead, it 
needs to be imported, just like any other class or function
```kotlin
import strings.lastChar // or import strings.*

fun main() {
    println("Kotlin".lastChar()) // n
}
```

## Scope functions
| Function | Receiver Object        | Return Value                    | Typical Use Case                                                                                                                      |
|----------|------------------------|---------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `also`   | `it` (object itself)    | Returns the object itself       | Performing side effects (logging, etc.) without changing the object. See [04. Lambdas](04_lambdas.md)                                 |
| `let`    | `it` (object itself)    | The last expression inside the block | Transforming an object and returning a result, especially with nullable types. See [06. Nullable](06_nullable.md)                     |
| `apply`  | `this` (object itself)  | Returns the object itself       | Mutating or modifying the object and returning it. See [04. Lambdas](04_lambdas.md)                                                   |
| `run`    | No receiver object      | The last expression inside the block | Performing actions on non-nullable objects or code blocks that return a result.                                                       |
| `with`   | No receiver object (explicit receiver) | The last expression inside the block | Performing actions on an object, using its properties and methods without modifying it. See Conditonals in [01. Basics](01_basics.md) |

**Examples**
- [Scope functions](../src/02/scopeFunctions.kt)

### Properties
```kotlin
val String.lastChar: Char // allows you to call "myText".lastChar
    get() = this.get(length - 1)
```

```kotlin
var StringBuilder.lastChar: Char // var because the contents of a StringBuilder can be modified.
    get() = this.get(length - 1) 
    set(value) { 
        this.setCharAt(length - 1, value)
    }

fun main() {
    val sb = StringBuilder("Kotlin?")
    println(sb.lastChar) // ?
    sb.lastChar = '!'
    println(sb) // Kotlin!
}
```

## Functions

### filter
Filter is built into [collections rather than the Java Stream API](https://kotlinlang.org/docs/java-to-kotlin-collections-guide.html#filter-elements) and returns the same collection type that was filtered
```kotlin
val positives = list.filter { x -> x > 0 }
val positives = list.filter { it > 0 } // shorter version
```

### in
Alternative to Java `contains`, `in` can be used to check presence of an element in a collection.
Also used to loop through a collection
```kotlin
if ("john@example.com" in emailsList) { ... }
if ("jane@example.com" !in emailsList) { ... }

for (email in emailsList) { ... }
```

### javaClass
Kotlin’s equivalent of Java’s `getClass()`.
```kotlin
fun main() {
    val set = setOf(1, 7, 53)
    val list = listOf(1, 7, 53)
    val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty -three")
    
    println(set.javaClass) 
    // class java.util.LinkedHashSet
    println(list.javaClass)
    // class java.util.Arrays$ArrayList
    println(map.javaClass)
    // class java.util.LinkedHashMap
}
```

### last(), shuffled(), sum()
```kotlin
fun main() {
    val strings = listOf("first", "second", "fourteenth")
    strings.last()
    // fourteenth
    println(strings.shuffled())
    // [fourteenth, second, first]
    
    val numbers = setOf(1, 14, 2)
    println(numbers.sum())
    // 17
}
```

### split, toRegex
In Kotlin, `split` is overloaded with versions that take a regular expression via type `Regex` or `Pattern` and plain `String`.
`toRegex` converts a String into a regular expression.
```kotlin
fun main() {
    // Regex
    println("12.345-6.A".split("\\.|-".toRegex()))  // [12, 345, 6, A]
    // String
    println("12.345-6.A".split(".", "-")) // [12, 345, 6, A]
}
```

Use Regex's `matchEntire` function to get a regex result.
You can use destructuring to assign the result of each group to a val
```kotlin
fun parsePathRegex(path: String) {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, filename, extension) = matchResult.destructured
        println("Dir: $directory, name: $filename, ext: $extension")
    }
}
fun main() {
    parsePathRegex("/Users/ry/kotlin-book/chapter.adoc")
    // Dir: /Users/ry/kotlin-book, name: chapter, ext: adoc
}
```

### substringBeforeLast, substringAfterLast
```kotlin
fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
    println("Dir: $directory, name: $fileName, ext: $extension")
}

fun main() {
    parsePath("/Users/ry/kotlin-book/chapter.adoc")
    // Dir: /Users/ry/kotlin-book, name: chapter, ext: adoc
}
```