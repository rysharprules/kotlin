# Collections
See [Basics](01_basics.md) for creating and accessing collections.

## Collection immutability
But every Java
collection interface has two representations in Kotlin: a read-only one and a mutable one.

<img src=../img/core/05/collection_hierarchy.png width=500 height=320>

`Map` class (which doesn’t extend `Collection` or `Iterable`) is also represented in Kotlin as two 
distinct versions: `Map` and `MutableMap`.

| Collection | Readonly                                   | Mutable                                                |
|------------|--------------------------------------------|--------------------------------------------------------|
| List       | listOf                                     | mutableListOf, MutableList, arrayListOf, buildList     |
| Set        | setOf                                      | mutableSetOf, hashSetOf, linkedSetOf, sortedSetOf, buildSet |
| Map        | mapOf                                      | mutableMapOf, hashMapOf, linkedMapOf, sortedMapOf, buildMap |

## Functions on Collections
- Does not use Java `Stream` API, but has similar functions.
- Calling a function such as `filter` on a collection returns a new collection with the elements that match the predicate - not a Stream.
- There is no need to `collect` the result.

### `associate`, `associateBy`, `associateWith`
The `associate` function turns a list into a map based on the pairs of keys and values returned by your lambda.

You use the infix function `to` to specify the individual key-value pairs
```kotlin
fun main() {
    val people = listOf(Person("Joe", 22), Person("Mary", 31))
    val nameToAge = people.associate { it.name to it.age }
    println(nameToAge) // {Joe=22, Mary=31}
    println(nameToAge["Joe"]) // 22
}
```
<img src=../img/core/05/associate.png width=475 height=360>

`associateWith` uses the original elements of your collection as keys. The
lambda you provide generates the corresponding value for each key. On the
other hand, `associateBy` uses the original elements of your collection as
values and uses your lambda to generate the keys of the map.

[associate example](../src/05/associate.kt)
[associateWith and associateBy examples](../src/05/associateWithBy.kt)

### `all`, `any`, `none`, `count` and `find`
These can be applied as predicates to collections.
#### `all`
Whether all the elements satisfy this predicate
```kotlin
fun main() {
    val canBeInClub27 = { p: Person -> p.age <= 27 }
    val people = listOf(Person("Alice", 27), Person("Bob", 31))
    println(people.all(canBeInClub27)) // false
}
```
`all` on an empty collection returns `true`.

`!all` (“not all”) with a condition can be replaced with `any`

#### `any`
Whether there’s at least one matching element
```kotlin
fun main() {
    val canBeInClub27 = { p: Person -> p.age <= 27 }
    val people = listOf(Person("Alice", 27), Person("Bob", 31))
    println(people.any(canBeInClub27)) // true
}
```
`any` on an empty collection returns `false`.

You can replace `!any` with `none`

#### `none`
```kotlin
fun main() {
    val list = listOf(1, 2, 3)
    println(!list.any { it == 4 }) // true
    println(list.none { it == 4 }) // true
}
```
#### `count`
How many elements satisfy a predicate
```kotlin
fun main() {
    val people = listOf(Person("Alice", 27), Person("Bob", 31))
    println(people.count(canBeInClub27)) // 1
}
```

#### `find`
Find an element that satisfies the predicate
```kotlin
fun main() {
    val people = listOf(Person("Alice", 27), Person("Bob", 31))
    println(people.find(canBeInClub27))
    // Person(name=Alice, age=27)
}
```
This returns `null` if nothing satisfies the predicate

### `chunked` and `windowed`

#### `chunked`
Break the collection into chunks of a certain size with `chunked`. The second argument allows for a lambda to apply to each chunk.
```kotlin
fun main() {
    val temperatures = listOf(27.7, 29.8, 22.0, 35.5, 19.1)
    println(temperatures.chunked(2)) // [[27.7, 29.8], [22.0, 35.5], [19.1]]
    println(temperatures.chunked(2) { it.sum() }) // [57.5, 57.5, 19.1]
}
```
<img src=../img/core/05/chunked.png width=475 height=360>

Note the final chunk may be smaller than the specified size if the list is out of elements.

#### `windowed`
Rather than breaking the collection into chunks, `windowed` creates a sliding window of a certain size. The second argument allows for a lambda to apply to each window.
```kotlin
fun main() {
    val temperatures = listOf(27.7, 29.8, 22.0, 35.5, 19.1)
    println(temperatures.windowed(3)) // [[27.7, 29.8, 22.0], [29.8, 22.0, 35.5], [22.0, 35. 5, 19.1]]
    println(temperatures.windowed(3) { it.sum() / it.size }) // [26.5, 29.099999999999998, 25.53333333333333]
}
```
<img src=../img/core/05/windowed.png width=500 height=400>

### `fold`

Similar to `reduce`, but with an initial value for the accumulator.

In cases where you want to retrieve all intermittent values of the `fold` operations you 
can use `runningFold`.

[Examples](../src/05/fold.kt)
[runningFold](../src/05/runningFold.kt)

### `filter`
The result is a new collection that contains only the elements from the input
collection that satisfy the predicate.
```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.filter { it % 2 == 0 }) // [2, 4]
}   
```
You can invert with `filterNot`.

This can be applied to Maps with `filterKeys` and `filterValues`.

[Examples](../src/05/filter.kt)

### `filterIndexed` and `mapIndexed`

Filter collections with the index of the element.

[Examples](../src/05/indexed.kt)

### `flatMap` and `flatten`
`flatMap` does two things: 
1. Transforms (or maps) each element to a collection, according to the function given as an argument (similar to `map`)
2. Combines (or flattens) these lists into one.

If you don’t need to transform anything and just need to flatten such a collection of collections, you can use the `flatten` function:
`listOfLists.flatten()`

[Examples](../src/05/flat.kt)

### `groupBy`
Create a `Map`, grouping the elements by a key determined by the lambda.
```kotlin
fun main() {
    val people = listOf(Person("Alice", 31), Person("Bob", 29), Person("Carol", 31))
    println(people.groupBy { it.age })
    // {31=[Person(name=Alice, age=31), Person(name=Carol, age=31)], 29=[Person(name=Bob, age=29)]}
}
```
Above the resulting map has the elements (`Person`s) grouped (by `age`)

For `true` and `false` results, use `partition`.

### `ifEmpty`
With the `ifEmpty` function, you can provide a lambda that generates a default value in case your collection does 
not contain any elements.
```kotlin
val empty = emptyList<String>()
println(empty.ifEmpty { listOf("no", "values", "here") }) // [no, values, here]
```

### `map`
Allows you to transform the elements of your input collection.
```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.map { it * it }) // [1, 4, 9, 16]
}
```

[Examples](../src/05/map.kt)

### `maxByOrNull`

Get the maximum element of a collection by a selector function. 

[Examples](../src/05/maxByOrNull.kt)

Calls can be chained:
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

### `partition`
Split the results of a Boolean predicate into two lists.

```kotlin
val people = listOf(Person("Alice", 26), Person("Bob", 29), Person("Carol", 31))
val (comeIn, stayOut) = people.partition(canBeInClub27)
println(comeIn) // [Person(name=Alice, age=26)]
println(stayOut) // [Person(name=Bob, age=29), Person(name=Carol, age=31)]
````
[More code with partition](../src/05/partition.kt)

### `reduce`

The `reduce` function gradually builds up a result in its accumulator, invoking your 
lambda repeatedly with each element and the previous accumulator value.

```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    val summed = list.reduce { acc, element -> acc + element }
    println(summed) // 10
}
```
In cases where you want to retrieve all intermittent values of the `reduce` operations you
can use `runningReduce`.

[More examples of reduce](../src/05/reduce.kt)
[runningReduce](../src/05/runningReduce.kt)

### `replaceAll` and `fill`
When applied to a `MutableList`, the `replaceAll` function replaces each element in the list with a 
result from the lambda.

For the special case of replacing all elements in the mutable list with the same value, you can
use the fill function as a shorthand.

[Examples](../src/05/replaceAll.kt)

### `zip`
You can use the `zip` function to create a list of pairs from values at the same index from
two collections. Passing a lambda to the function also allows you to specify how the output 
should be transformed.

```kotlin
fun main() {
    val names = listOf("Joe", "Mary", "Jamie")
    val ages = listOf(22, 31, 31, 44, 0)
    println(names.zip(ages))
    // [(Joe, 22), (Mary, 31), (Jamie, 31)]
    println(names.zip(ages) { name, age -> Person(name, age) })
    // [Person(name=Joe, age=22), Person(name=Mary, age=31), Person(name=Jamie, age=31)]
}
```
<img src=../img/core/05/zip.png width=475 height=550>

Values that don’t have a counterpart in the other collection are ignored by zip.

[Examples](../src/05/zip.kt)

## Sequences
Sequences allow for lazy operations on collections. They are similar to Java Streams as they are not run
without a terminal operation.

Use `asSequence` to convert a collection to a sequence and `toList` to convert back to a collection.

```kotlin
people
    .asSequence() // Converts the initial collection to Sequence
    // Sequences support the same API as collections.
    .map(Person::name) 
    .filter { it.startsWith("A") } 
    .toList() // Converts the resulting Sequence back into a list
```

### Lazy evaluation
```kotlin
fun main() {
    println(
        listOf(1, 2, 3, 4)
            .asSequence()
            .map { it * it }
            .find { it > 3 }
    ) // 4
}
```
Eager evaluation runs each operation on the entire collection; lazy evaluation processes elements one by one.

<img src=../img/core/05/sequence.png width=375 height=260>

### Generating sequences
`generateSequence` calculates the next element in a sequence, given the previous one.
`takeWhile` function to take elements from that sequence if they satisfy a certain condition.
```kotlin
fun main() {
    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    // All the delayed operations are performed when the result sum is obtained.
    println(numbersTo100.sum()) // 5050
}
```

```kotlin
import java.io.File

// using sequences allows you to stop traversing the parents as soon as you find the required directory
fun File.isInsideHiddenDirectory() = generateSequence(this) { 
    it.parentFile }.any { it.isHidden 
    }

fun main() {
    val file = File("/Users/rydawg/.HiddenDir/a.txt")
    println(file.isInsideHiddenDirectory()) // true
}
```