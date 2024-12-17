# Basics

- The main entry point of a Kotlin program is the `main` function. This does not require the input arguments, i.e. `fun main(args: Array<String>)`
- `;` is not required at the end of a line
- There's no `new` keyword in Kotlin
- Comments are the same as most languages, single line `//` or multi-line `/*` `*/`
- There are no 'Checked' exceptions and there is no `throws` keyword
- Kotlin does not have `static` types. Top-level functions (outside of any class) are global and can be accessed in a static-like way
- The directory and package hierarchy do not need to match, but it is good practice that they do

**Examples**
- [hello world](src/01/helloworld.kt) :)
- [Reading input](src/01/input.kt)

## Functions

use the `fun` keyword
```kotlin
fun main() {
    ...
}
```

The types come after the argument name and a colon `:`. The return type comes after the arguments.
```kotlin
fun sum(a: Int, b: Int): Int {
    return a + b
}
```

A function body can be an expression. Its return type is inferred:
```kotlin
fun sum(a: Int, b: Int) = a + b
```

## Variables

- Read-only variables with `val`
- Mutable variables with `var`

Assign with `=` operator. Kotlin will _infer_ the type (similar to `var` in Java):
```kotlin
val customers = 10
var x: Int = 5 // You can declare the type. Without initialization; type is required
val yearsToCompute = 7.5e6 // (Double)
```

## String templates

- Include a variable in a String with the dollar sign `$`
- Include an expression in a String with a dollar sign and curly braces `${...}`

```kotlin
val customers = 10
println("There are $customers customers")
// There are 10 customers

println("There are ${customers + 1} customers")
// There are 11 customers
```

Escape with backslash: `println("The price is \$9.99")`

Note the value of a variable in a String template does not change even if the variable itself changes:

```kotlin
    var a = 1
    val s1 = "a is $a" 
    println(s1) // a is 1
    
    a = 2
    println(a) // 2
    println(s1) // a is 1
```

## Enums
`enum` is a "soft keyword", i.e. it can be used as a function, variable name, or parameter.
`class` keyword is required here
No need for `;` to end the list of enums unless properties are declared
```kotlin
enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}
```

**Examples**
- [Enum with properties](src/01/enums/withProperties.kt)
- [Looping enum with when](src/01/enums/looping.kt)
- [Looping enum with when with multiple values in teh same branch](src/01/enums/loopingCombined.kt)

## Classes

Classes are immutable and so not require boilerplate code for getters/setters, similar to `record`s.

```kotlin
class Rectangle(val height: Double, val length: Double) {
    val perimeter = (height + length) * 2
}
```

### Getters and Setters

#### Creation
- If the type is `val` then the property is immutable so only a 'getter' is available
- If the type is `var` then the property is mutable so both 'getter' and 'setter' are available

#### Use
Refer to the property name (not `getXXX`)
```kotlin
class Address {
    var name: String = "Holmes, Sherlock"
    var street: String = "Baker"
    var city: String = "London"
    var state: String? = null
    var zip: String = "123456"
    var isValid: Boolean = true
}

fun copyAddress(address: Address): Address {
    val result = Address() // there's no 'new' keyword in Kotlin
    result.name = address.name // accessors are called
    result.street = address.street
    // ...
    address.state = "New York" // set the value on the property directly
    return result
}
```

#### Custom getter
```kotlin
class Rectangle(val width: Int, val height: Int) {
    val area: Int // property type is optional since it can be inferred from the getter's return type
        get() = this.width * this.height
}
```
Or more succinctly `val area get() = this.width * this.height`

### Inheritance

Declared by a colon `:`. Classes are final by default; to make a class inheritable, mark it as `open`:

```kotlin
open class Shape

class Rectangle(val height: Double, val length: Double): Shape() {
    val perimeter = (height + length) * 2
}
```

## Conditionals

### if
`if` can be used as an expression, i.e. it returns a value, similar to ternary operator:
```kotlin
fun maxOf(a: Int, b: Int) = if (a > b) a else b`
```

### for
Similar to for-each:
```kotlin
val items = listOf("apple", "banana", "kiwifruit")
for (item in items) {
    println(item)
}
// apple
// banana
// kiwifruit
```

```kotlin
val items = listOf("apple", "banana", "kiwifruit")
for (index in items.indices) {
    println("item at $index is ${items[index]}")
}
// item at 0 is apple
// item at 1 is banana
// item at 2 is kiwifruit
```

There is no natural `for (int i = 0; i < XXX; i++)` construct. See Ranges later in this page.

### while
```kotlin
val items = listOf("apple", "banana", "kiwifruit")
var index = 0
while (index < items.size) {
    println("item at $index is ${items[index]}")
    index++
}
// item at 0 is apple
// item at 1 is banana
// item at 2 is kiwifruit
```

#### Labels
For nested loops you can label with `@` to `break` or `continue`
```kotlin
outer@ while (outerCondition) { 
    while (innerCondition) {
        if (shouldExitInner) break
        if (shouldSkipInner) continue 
        if (shouldExit) break@outer 
        if (shouldSkip) continue@outer // ...
    }  
// ...
}
```

### when
Similar to Java `switch`. This is an expression so the value is returned with no fall-through.
Accepts any type of object, e.g. primitives (Int, String) and custom classes using the `equals()` by default.
Use the `else` wildcard to match anything. This is required if returning a value if the other cases do not cover all possibilities. `else` is comparable to the `switch` `default`.
```kotlin
fun describe(obj: Any): String =
    when (obj) {
        1          -> "One"
        "Hello"    -> "Greeting"
        is Long    -> "Long"
        !is String -> "Not a string"
        null       -> "is null"
        else       -> "Unknown"
    }
```

For `is` see Type checks in this page.

## Ranges
Can be used as alternative to classic `for (int i = 0; i < XXXX; i++)`
```kotlin
for (x in 1..5) {
    print(x)
}
```

`downTo` can reverse the direction
`step` can step over (or skip) the progression
```kotlin
for (x in 1..10 step 2) { // progresses up, skipping two each time
    print(x)
}
// 13579
for (x in 9 downTo 0 step 3) { // progresses down, three at a time
    print(x)
}
// 9630
```

Doesn't have to be used just in loops
```kotlin
val x = 10
val y = 9
if (x in 1..y+1) {
    println("fits in range")
}
```

**Examples**
- [Fizzbuzz](src/01/range/fizzbuzz.kt)
- [Validation with ranges](src/01/range/validation.kt) including characters `in 'a'..'z' and opposites with `!in`

## Collections
Kotlin has `List`s, `Set`s, and `Map`s.

### Creation
#### Immutable
```kotlin
val fruits = listOf("Apple", "Banana", "Orange")
println(fruits[0]) // Output: Apple

val numbers = setOf(1, 2, 3, 4, 5)
println(numbers.contains(3)) // Output: true

val countryCapital = mapOf("USA" to "Washington, D.C.", "France" to "Paris", "Japan" to "Tokyo")
println(countryCapital["USA"]) // Output: Washington, D.C.
```

#### Mutable
```kotlin
val mutableFruits = mutableListOf("Apple", "Banana", "Orange")
mutableFruits.add("Mango") // Adds Mango to the list
mutableFruits.remove("Banana") // Removes Banana from the list
println(mutableFruits) // Output: [Apple, Orange, Mango]

val mutableNumbers = mutableSetOf(1, 2, 3, 4, 5)
mutableNumbers.add(6) // Adds 6 to the set
mutableNumbers.remove(4) // Removes 4 from the set
println(mutableNumbers) // Output: [1, 2, 3, 5, 6]

val mutableCountryCapital = mutableMapOf("USA" to "Washington, D.C.", "France" to "Paris")
mutableCountryCapital["Germany"] = "Berlin" // Adds Germany to the map
mutableCountryCapital.remove("France") // Removes France from the map
println(mutableCountryCapital) // Output: {USA=Washington, D.C., Germany=Berlin}
```

#### Using Ranges
```kotlin
val rangeList = (1..5).toList() // Creates a list from 1 to 5
println(rangeList) // Output: [1, 2, 3, 4, 5]

val rangeSet = (1..5).toSet() // Creates a set from 1 to 5
println(rangeSet) // Output: [1, 2, 3, 4, 5]
```

### Functions on Collections
Lambda expressions can be used
```kotlin
val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
fruits
    .filter { it.startsWith("a") }
    .sortedBy { it }
    .map { it.uppercase() }
    .forEach { println(it) }
// APPLE
// AVOCADO
```

**Examples**
- [Map](src/01/collections/map.kt)

## Null
A reference must be explicitly marked as nullable when null value is possible with `?`
```kolin
fun parseInt(str: String): Int? {
```

## Type checks
`is` is used for type checks, similar to `instanceof`
Like Java 16s "Pattern Matching", Kotlin employs "Smart Casts" to automatically cast a field when it is determined to be of that type:
```kotlin
if (obj is String) {
    return obj.length // obj is automatically cast to String
}
```

**Examples**
- [Sum operation with smart casts](src/01/smartcast/sumOperation.kt)
