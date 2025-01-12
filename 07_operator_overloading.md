# Operator overloading and other conventions

## Overloadable binary arithmetic operators

| Expression | Function Name |
|------------|---------------|
| a * b      | times         |
| a / b      | div           |
| a % b      | mod           |
| a + b      | plus          |
| a - b      | minus         |

### `plus`

<img src=img/07_plus.png width=240 height=30>

```kotlin
data class Point(val x: Int, val y: Int) {
    // as a member function
    operator fun plus(other: Point): Point { 
        return Point(x + other.x, y + other.y)  
    }
}

fun main() {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2) // Point(x=40, y=60)
}
```

### `times`

```kotlin
// as an extension function
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

fun main() {
    val p = Point(10, 20)
    println(p * 1.5) // Point(x=15, y=30)
}
```

If you want users to be able to write `1.5 * p` in addition to `p * 1.5`, you need to define a separate
operator for that: `operator fun Double.times(p: Point): Point`.

## Ordering operators `compareTo` (`<`, `<=`, `>`, `>=`)
Kotlin supports `Comparable` interface. But the `compareTo` Comparison operators (`<`, `>`, `<=`, and `>=`) 
are translated into calls of `compareTo`.

The expression `p1 < p2` is equivalent to `p1.compareTo(p2) < 0`.
```kotlin
class Person(val firstName: String, val lastName: String) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        // compareValuesBy function from the Kotlin standard library
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}

fun main() {
    val p1 = Person("Alice", "Smith")
    val p2 = Person("Bob", "Johnson")
    println(p1 < p2) // false
}
```

All classes that implement the `Comparable` interface can be compared in Kotlin using the concise operator syntax:
```kotlin
fun main() {
    println("abc" < "bac") // true
}
```

### `compareByValues`

The `compareValuesBy` function receives a list of selector functions that calculate values to be
compared. The function calls each selector in order for both objects and compares the return values.

If the values are different, it returns the result of the comparison. If they’re the same, it proceeds 
to the next selector function, or returns `0` if there are no more functions to call.

## Collections and ranges

### Accessing elements by index: The `get` and `set` conventions

<img src=img/07_compare.png width=280 height=31>

You can access the elements in a map similarly to how you access arrays in Java—via square brackets:
`val value = map[key]`.

You can use the same operator to change the value for a key in a mutable map:
`mutableMap[key] = newValue`.

Reading an element using the indexed access operator is translated into a call of the `get` operator 
method, and writing an element becomes a call to `set`. The methods defined for the `Map` and
`MutableMap` interfaces.

**Examples:**
- [Add get operator to custom Point class](src/07/get.kt).
- [Add set operator to custom mutable Point class](src/07/set.kt).

### Checking whether an object belongs to a collection: The `in` convention

<img src=img/07_contains.png width=240 height=30>

The `in` operator is used to check whether an object belongs to a collection. The corresponding function
is called `contains`.

[Example of in operator in custom Rectangle class](src/07/in.kt).

### Creating ranges from objects: The `rangeTo` and `rangeUntil` conventions
The `..` operator is transformed into a `rangeTo` function call.

The `rangeTo` function returns a range and is available for any `Comparable` type.

<img src=img/07_range.png width=250 height=35>

The `rangeUntil` operator (`..<`) returns an open-end range, which doesn’t include the specified 
upper bound.

[Examples of .. and ..< operators](src/07/rangeTo.kt).

## Destructuring declarations
Get multiple values from an object using destructuring declarations.

<img src=img/07_deconstructing.png width=450 height=50>

```kotlin
fun main() {
    val p = Point(10, 20)
    val (x, y) = p 
    println(x) // 10
    println(y) // 20
}
```

If you wish to ignore a value, convention is to use an underscore `_`.

**Examples:**
- Full [example of deconstructing the Point class](src/07/deconstructing/deconstructing.kt)
- [Splitting a file name](src/07/deconstructing/splitFilename.kt)
- [Destructuring declaration in a loop](src/07/deconstructing/loops.kt)

## Delegated properties: `by` and `lazy`
Lazy initialization is a common pattern that entails creating part of an object on
demand when it’s accessed for the first time.

Consider a `Person` class that lets you access a list of the emails
written by a person. The emails are stored in a database and take a long time to
access. You want to load the emails on first access to the property and do so
only once:
```kotlin
class Email { /*...*/ }
fun loadEmails(person: Person): List<Email> {
    println("Load emails for ${person.name}")
    return listOf(/*...*/)
}

class Person(val name: String) {
    // The _emails property, which stores the data and to which emails delegates
    private var _emails: List<Email>? = null 
    val emails: List<Email>
        get() {
            if (_emails == null) {
                // Loads the data on access
                _emails = loadEmails(this)  
            }
            return _emails!! // If the data was loaded before, it returns it
        }
}

fun main() {
    val p = Person("Alice")
    p.emails  // Emails are loaded on first access. Load emails for Alice
    p.emails
}
```

A delegated property, which can encapsulate both the backing property used to store the value and 
the logic ensuring that the value is initialized only once. The delegate you can use here is
returned by the `lazy` standard library function.

```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```

Also see:
- `kotlin.properties.Delegates` - Observable properties class
- `kotlin.reflect.KProperty` - Kotlin’s delegated property feature