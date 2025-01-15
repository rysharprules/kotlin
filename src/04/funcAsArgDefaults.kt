package `04`

fun <T> Collection<T>.joinToString1(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element) // Converts the object to a string, using the default toString method
    }
    result.append(postfix)
    return result.toString()
}

// Refactored:
// you can pass a lambda to specify how values are converted into strings. But requiring all callers to pass that lambda
// would be cumbersome because most of them are okay with the default behavior (toString)
// Define a parameter of a function type and specify a default value for it as a lambda.
fun <T> Collection<T>.joinToString2(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    // Declares a parameter of a function type with a lambda as a default value
    transform: (T) -> String = { it.toString() }
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element)) // Calls the function passed as an argument for the transform parameter
    }
    result.append(postfix)
    return result.toString()
}

// A shorter version makes use of the fact that a function type is an implementation of an interface with an invoke
// method. As a regular method, invoke can be called through the safe-call syntax: callback?.invoke().
fun <T> Collection<T>.joinToString3(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: ((T) -> String)? = null // Declares a nullable parameter of a function type
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        // Uses the safe-call syntax to call the function
        val str = transform?.invoke(element) ?: element.toString() // Uses the Elvis operator to handle the case in which a callback wasn’t specified
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}

fun main() {
    val letters = listOf("Alpha", "Beta")

    // Uses the default conversion function
    println(letters.joinToString2()) // Alpha, Beta

    // Passes a lambda as an argument
    println(letters.joinToString2 { it.lowercase() }) // alpha, beta

    // Uses the named argument syntax for passing several arguments, including a lambda
    println(letters.joinToString2(separator = "! ", postfix = "! ",
        transform = { it.uppercase() })) // ALPHA! BETA!
}