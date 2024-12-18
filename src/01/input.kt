package `01`

fun main() {
    // Prints a message to request input
    println("Enter any word: ")

    // Reads and stores the user input. For example: Happiness
    val yourWord = readln()

    // Prints a message with the input
    print("You entered the word: ")
    print(yourWord)
    // You entered the word: Happiness

    // Reads a string and returns null if the input can't be converted into an integer.
    val intOrNull = readln().toIntOrNull()
    println(intOrNull)
}