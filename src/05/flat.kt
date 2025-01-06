package `05`

class Book(val title: String, val authors: List<String>)

val library = listOf(
    Book("Kotlin in Action", listOf("Isakova", "Elizarov", "Aigner", "Jemerov")),
    Book("Atomic Kotlin", listOf("Eckel", "Isakova")),
    Book("The Three-Body Problem", listOf("Liu"))
)

fun main() {
    val authors = library.map { it.authors }
    // List<List<String>>
    println(authors) // [[Isakova, Elizarov, Aigner, Jemerov], [Eckel, Isakova], [Liu]]

    val authors2 = library.flatMap { it.authors }
    // List<String>
    println(authors2) // [Isakova, Elizarov, Aigner, Jemerov, Eckel, Isakova, Liu]
    // Set<String> no duplicates
    println(authors2.toSet()) // [Isakova, Elizarov, Aigner, Jemerov, Eckel, Liu]

    val authors3 = authors.flatten()
    println(authors3) // [Isakova, Elizarov, Aigner, Jemerov, Eckel, Isakova, Liu]
}