package `01`.collections

/*
Mutable maps preserve element iteration order in Kotlin.
Iterates over the characters from A to F, using a range of characters
Converts ASCII code to binary
Stores the value in a map by the c key
Iterates over a map, assigning the map key to letter and the associated value
to binary
 */
fun main() {
    val binaryReps = mutableMapOf<Char, String>()
    for (char in 'A'..'F') {
        val binary = char.code.toString(radix = 2)
        binaryReps[char] = binary
    }
    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }
    // A = 1000001 D = 1000100
    // B = 1000010 E = 1000101
    // C = 1000011 F = 1000110
    // (output split into columns for conciseness)
}