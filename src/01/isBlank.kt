package `01`

fun main() {
    val blankName = " "
    val name = "J. Doe"
    println(blankName.ifEmpty { "(unnamed)" }) //
    println(blankName.ifBlank { "(unnamed)" }) // (unnamed)
    println(name.ifBlank { "(unnamed)" }) // J. Doe
}