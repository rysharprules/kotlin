# Basics

- The root of the Kotlin class hierarchy is [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/). Every Kotlin class has `Any` as a superclass.
- The main entry point of a Kotlin program is the `main` function. This does not require the input arguments, i.e. `fun main(args: Array<String>)`
- `;` is not required at the end of a line
- There's no `new` keyword in Kotlin
- Comments are the same as most languages, single line `//` or multi-line `/*` `*/`
- There are no 'Checked' exceptions and there is no `throws` keyword
- Kotlin does not have `static` types. Top-level functions (outside of any class) are global and can be accessed in a static-like way
- [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/) is synonymous with `void` in Java
- Kotlin's standard library has a `TODO()` function that will always throw a `NotImplementedError`
- Functions can return `Nothing`. It has no value. It is used to indicate that the function never returns normally.

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
- Compile-time constants `const`

Assign with `=` operator. Kotlin will _infer_ the type (similar to `var` in Java):
```kotlin
val customers = 10
var x: Int = 5 // You can declare the type. Without initialization; type is required
val yearsToCompute = 7.5e6 // (Double)
```

Names of constants (properties marked with const, or top-level or object val properties with no custom get function that 
hold deeply immutable data) should use all uppercase, underscore-separated names following the (screaming snake case)
```kotlin
const val MAX_COUNT = 8
val USER_NAME_FIELD = "UserName"
```

## Strings 

### String templates

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

### Triple-quoted Strings
This can be useful for JSON, HTML etc.
```kotlin
val kotlinLogo =
    """
    | //
    |//
    |/ \
    """.trimIndent()
```
`trimIndent` - Removes that common minimal indent of all the lines of your string and remove the first and last lines of the string, given they are blank.

You don’t have to escape `\`, so the Windows-style path `C:\\Users\\yole\\kotlin-book` can be written as `"""C:\Users\yole\kotlin-book"""`

### `isBlank`
Checks if a String consists only of whitespace characters. See [examples](src/01/isBlank.kt).

## Unsigned number types
Unsigned number types “shift” the value range compared to their signed counterparts, allowing you to 
store larger non-negative numbers in the same amount of memory.

| Type   | Size    | Value range        |
|--------|---------|--------------------|
| UByte  | 8 bit   | 0 - 255            |
| UShort | 16 bit  | 0 - 65535          |
| UInt   | 32 bit  | 0 - 2^32 - 1       |
| ULong  | 64 bit  | 0 - 2^64 - 1       |
<img src=img/01_ubyte.png width=600 height=170>

## Arrays

```kotlin
fun main(args: Array<String>) {
    // Uses the array.indices extension property to iterate over the range of indices
    for (i in args.indices) { 
        println("Argument $i is: ${args[i]}") // Accesses elements by index with array[index]
    }
}
```

- The `arrayOf` function creates an array containing the elements specified as arguments to this function.
- The `arrayOfNulls` function creates an array of a given size containing `null` elements.
- The `Array` constructor takes the size of the array and a lambda and initializes each array element 
by calling the lambda. This is how you can initialize an array with a non-null element type without passing
each element explicitly:
```kotlin
fun main() {
    val letters = Array<String>(26) { i -> ('a' + i).toString() }
    // element type can be omitted
    // val letters = Array(26) { i -> ('a' + i).toString() }
    println(letters.joinToString("")) // abcdefghijklmnopqrstuvwxyz
}
```

To represent arrays of primitive types, Kotlin provides a number of separate classes—one for each 
primitive type. For example, an array of values of type `Int` is called `IntArray`. For other types, 
Kotlin provides `ByteArray`, `CharArray`, `BooleanArray`, and so on.

### Convert to Array with `toTypedArray()`
```kotlin
fun main() {
    val strings = listOf("a", "b", "c")
    // The spread operator (*) is used to pass an array when vararg parameters are expected.
    println("%s/%s/%s".format(*strings.toTypedArray())) // a/b/c
}
```

## Bitwise operations
Kotlin doesn’t define any bitwise operators for standard number types.
It uses regular functions supporting the infix call syntax:
- shl —Signed shift left
- shr —Signed shift right
- ushr —Unsigned shift right
- and —Bitwise and
- or —Bitwise or
- xor —Bitwise xor
- inv —Bitwise inversion

```kotlin
fun main() {
    println(0x0F and 0xF0) // 0
    println(0x0F or 0xF0) // 255
    println(0x1 shl 4) // 16
}
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
- [Looping enum with when with multiple values in the same branch](src/01/enums/loopingCombined.kt)

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

##### Custom accessor visibility

By default, the accessor’s visibility is the same as the property’s
```kotlin
class LengthCounter {
    var counter: Int = 0 // counter is public
        private set 
    
    fun addWord(word: String) {
        counter += word.length
    }
}

fun main() {
    val lengthCounter = LengthCounter()
    lengthCounter.addWord("Hi!")
    println(lengthCounter.counter) // 3

    lengthCounter.counter = 0 // Error: Cannot assign to 'counter': the setter is private in 'LengthCounter'
}
```

##### Accessing backing field from a getter

In the body of the setter, you use the special identifier `field` to access the
value of the backing field
In a getter, you can only read the value.
```kotlin
class User(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println( // Reads the backing field value
                """
                Address was changed for $name:
                "$field" -> "$value".
                """.trimIndent()
            )
            field = value // Updates the backing field value with the provided string
    }
}

fun main() {
    val user = User("Alice")
    user.address = "Christoph-Rapparini-Bogen 23"
    // Address was changed for Alice:
    // "unspecified" -> "Christoph-Rapparini-Bogen 23".
}
```

Backing field auto-creation by compiler:
- The compiler will generate the backing field for the property if you either reference it explicitly or use
  the default accessor implementation.
- If you provide custom accessor implementations that don’t use `field` (for the getter if the property is 
 a `val` and for both accessors if it’s a mutable property), the compiler understands that the property 
 doesn’t need to store any information itself, so no backing field will be generated.

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

#### Guard conditions
Guard conditions allow you to include more than one condition to the branches of a when expression, making complex control flow more explicit and concise.
To include a guard condition in a branch, place it after the primary condition, separated by `if`
```kotlin
sealed interface Animal {
    data class Cat(val mouseHunter: Boolean) : Animal
    data class Dog(val breed: String) : Animal
}

fun feedAnimal(animal: Animal) {
    when (animal) {
        // Branch with only primary condition. Calls `feedDog()` when `Animal` is `Dog`
        is Animal.Dog -> feedDog()
        // Branch with both primary and guard conditions. Calls `feedCat()` when `Animal` is `Cat` and is not `mouseHunter`
        is Animal.Cat if !animal.mouseHunter -> feedCat()
        // Calls giveLettuce() if none of the above conditions match and animal.eatsPlants is true
        else if animal.eatsPlants -> giveLettuce()
        // Prints "Unknown animal" if none of the above conditions match
        else -> println("Unknown animal")
    }
}
```

Combine multiple guard conditions within a single branch using the boolean operators `&&` (AND) or `||` (OR). Use parentheses around the boolean expressions to avoid confusion
```kotlin
when (animal) {
    is Animal.Cat if (!animal.mouseHunter && animal.hungry) -> feedCat()
}
```

## Ranges
Can be used as alternative to classic `for (int i = 0; i < XXXX; i++)`
```kotlin
for (x in 1..5) {
    print(x)
}

(1..10).forEach { ... }
```

Note the range is `1` to `5` inclusive. To exclude the last value, use `until` or make it an _open range_ with `<` operator
which means the range is `1` to `4`.

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

You can create empty lists with `emptyList`:
```kotlin
fun main() {
    println(emptyList<Int>().any { it > 42 }) // false
}
```

### Accessing
#### Lists
```kotlin
val element = countries[2]
// The index operator [] is less noisy than the get() method and is more convenient
val element = countries.get(3)
```

Additionally, there are helper methods to get the first and last items
```kotlin
countries.first()
countries.last()
countries.first { it.length > 7 }
countries.last { it.startsWith("J") }
countries.firstOrNull { it.length > 8 }
```

#### Sets
You cannot access elements in a `Set` by index because it is unordered.
If you just need an element from the set, you can use methods like `first()`, `last()`, or `random()`
Use iteration (`for`, `forEach`) or membership checks (`contains` or `in`)
```kotlin
val set = setOf(1, 2, 3, 4)

// Iterate through the set
set.forEach { println(it) }

// Check for membership
if (3 in set) {
    println("3 is in the set")
}
```

#### Maps
```kotlin
println(map["key"])
map["key"] = value
```

### Traversing
#### Lists and Sets
```kotlin
for (country in countries) {
    country.length
    // ...
}

for (i in 0 until countries.size) {
    countries[i].length
    // ...
}

countries.forEach { it ->
    it.length
    // ...
}

// forEachIndexed() method performs an action on each element along with providing a sequential index with the element.
countries.forEachIndexed { i, e ->
    e.length
    // ...
}
```
#### Maps
```kotlin
for ((k, v) in map) {
    println("$k -> $v")
}
```

More on [Collections](05_collections.md)

#### Using Ranges
```kotlin
val rangeList = (1..5).toList() // Creates a list from 1 to 5
println(rangeList) // Output: [1, 2, 3, 4, 5]

val rangeSet = (1..5).toSet() // Creates a set from 1 to 5
println(rangeSet) // Output: [1, 2, 3, 4, 5]
```

**Examples**
- [Map](src/01/collections/map.kt)

## Null
A reference must be explicitly marked as nullable when null value is possible with `?`
```kolin
fun parseInt(str: String): Int? {
```

### If-not-null shorthand
```kotlin
val files = File("Test").listFiles()
println(files?.size) // size is printed if files is not null

// If-not-null-else
println(files?.size ?: "empty") // if files is null, this prints "empty"

// To calculate a more complicated fallback value in a code block, use `run`
val filesSize = files?.size ?: run {
    val someSize = getSomeSize()
    someSize * 2
}
println(filesSize)

// Execute a statement if null
val values = ...
val email = values["email"] ?: throw IllegalStateException("Email is missing!")
```

More on [Nullable](06_nullable.md)

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
