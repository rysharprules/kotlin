package `03`

/*
Delegation support is a language feature in Kotlin.

Whenever you’re implementing an interface, you can say that you’re delegating the implementation of the interface to
another object, using the by keyword.

The compiler will generate all the method implementations

When you need to change the behavior of some methods, you can override them, and your code will be called instead of the generated methods.
 */
class CountingSet<T>(
    // Delegates the MutableCollection implementation to innerSet
    private val innerSet: MutableCollection<T> = hashSetOf<T>()) : MutableCollection<T> by innerSet {
        var objectsAdded = 0

        // Provides a different implementation instead of directly delegating
        override fun add(element: T): Boolean {
             objectsAdded++
            return innerSet.add(element)
        }

        // Provides a different implementation instead of directly delegating
        override fun addAll(elements: Collection<T>): Boolean {
            objectsAdded += elements.size
            return innerSet.addAll(elements)
        }
}

fun main() {
    val cset = CountingSet<Int>()
    cset.addAll(listOf(1, 1, 2))
    println("Added ${cset.objectsAdded} objects, ${cset.size} uniques.")
    // Added 3 objects, 2 uniques.
}