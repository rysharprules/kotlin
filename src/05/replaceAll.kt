package `05`

fun main() {
    val names = mutableListOf("Martin", "Samuel")
    println(names) // [Martin, Samuel]
    names.replaceAll { it.uppercase() }
    println(names) // [MARTIN, SAMUEL]
    names.fill("(redacted)")
    println(names) // [(redacted), (redacted)]
}