# Collections
See [Basics](01_basics.md) for creating and accessing collections.

## Functions on Collections
- Does not use Java `Stream` API, but has similar functions.
- Calling a function such as `filter` on a collection returns a new collection with the elements that match the predicate - not a Stream.
- There is no need to `collect` the result.

### `filter`
The result is a new collection that contains only the elements from the input
collection that satisfy the predicate.
```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.filter { it % 2 == 0 }) // [2, 4]
}   
```

This can be applied to Maps with `filterKeys` and `filterValues`.

[Examples](src/05/filter.kt)

### `filterIndexed` and `mapIndexed`

Filter collections with the index of the element.

[Examples](src/05/indexed.kt)

### `map`
Allows you to transform the elements of your input collection.
```kotlin
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.map { it * it }) // [1, 4, 9, 16]
}
```

[Examples](src/05/map.kt)

### `maxByOrNull`

Get the maximum element of a collection by a selector function. 

[Examples](src/05/maxByOrNull.kt)

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