package `07`.deconstructing

fun printEntries(map: Map<String, String>) {
    for ((key, value) in map) { // Destructuring declaration in a loop
        println("$key -> $value")
    }
}

fun main() {
    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    printEntries(map)
    // Oracle -> Java
    // JetBrains -> Kotlin
}