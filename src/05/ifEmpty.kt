package `05`

fun main() {
    val empty = emptyList<String>()
    val full = listOf("apple", "orange", "banana")
    println(empty.ifEmpty { listOf("no", "values", "here") }) // [no, values, here]
    println(full.ifEmpty { listOf("no", "values", "here") }) // [apple, orange, banana]
}