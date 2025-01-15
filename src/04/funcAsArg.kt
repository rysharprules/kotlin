package `04`

fun twoAndThree(operation: (Int, Int) -> Int) { // Declares a parameter of a function type
    val result = operation(2, 3) // Calls the parameter of a function type
    println("The result is $result")
}

fun twoAndThreeNamed(
    operation: (operandA: Int, operandB: Int) -> Int) { // The function type now has named parameters.
    val result = operation(2, 3)
    println("The result is $result")
}

fun main() {
    twoAndThree { a, b -> a + b } // The result is 5
    twoAndThree { a, b -> a * b } // The result is 6

    // You can use the names provided in the API as lambda argument names
    twoAndThreeNamed { operandA, operandB -> operandA + operandB }  // The result is 5
    // .. or you can change them.
    twoAndThreeNamed { alpha, beta -> alpha + beta }  // The result is 5
}