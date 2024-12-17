package `01`.range

// c in 'a'..'z' transforms to 'a' <= c && c <= 'z'
fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'..'9'

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter!"
    else -> "I don't know..."
}
fun main() {
    println(isLetter('q'))
    // true
    println(isNotDigit('x'))
    // true

    println(recognize('8'))
    // It's a digit!

    // The same as “Java” <= “Kotlin” && “Kotlin” <= “Scala”
    println("Kotlin" in "Java".."Scala") // true

    // The same in check works with collections as well
    println("Kotlin" in setOf("Java", "Scala"))
    // false
}