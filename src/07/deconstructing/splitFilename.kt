package `07`.deconstructing

// Declares a data class to hold the values
data class NameComponents(val name: String, val extension: String)

fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    // Returns an instance of the data class from the function
    return NameComponents(result[0], result[1])
}

// You can improve this example even further if you note that componentN
// functions are also defined on arrays and collections
fun splitFilenameRefactor1(fullName: String): NameComponents {
    val (name, extension) = fullName.split('.', limit = 2)
    return NameComponents(name, extension)
}

fun main() {
    // Uses the destructuring declaration syntax to unpack the class
    val (name, ext) = splitFilename("example.kt")
    println(name) // example
    println(ext) // kt
}