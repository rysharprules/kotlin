package `02`

fun main() {
    // 1. let: Useful for transforming objects or working with nullable types
    // let is used here to safely transform the name (nullable string). If name is non-null, it calculates and returns its length.
    val name: String? = "Alice"
    val length = name?.let {
        println("Length of $it is being calculated.")
        it.length  // Transforms the object and returns a value
    }
    println("Length: $length")  // Prints the length of "Alice"

    // 2. apply: Used to modify an object's properties and return the object itself
    // apply is used to modify the properties of the person object (increasing the age) and log information. It returns the modified person object itself.
    val person = Person("Bob", 30).apply {
        age += 1  // Mutates the object
        println("Name: $name, Age: $age")  // Side effect: logging
    }
    println("Updated person: $person")  // Prints the modified object

    // 3. run: Executes a block and returns the result of the last expression
    // run is used to execute a block of code that calculates and returns the sum of two integers.
    val result = run {
        val a = 5
        val b = 10
        a + b  // The result of this is returned
    }
    println("Result of sum: $result")  // Prints the sum of a and b

    // 4. also: Performs side effects without changing the object, and returns the object itself
    // also is used to log the number (perform a side effect) without altering its value. It returns the original number.
    val number = 10
    val newNumber = number.also {
        println("Logging the number: $it")  // Side effect: logging
    }
    println("Original number: $newNumber")  // Prints the original number (10)

    // 5. with: Used to operate on an object, using its properties and methods
    // with is used to operate on the student object to create a string containing the student's name and age. Unlike apply, you do not use this explicitly inside the block.
    val student = Student("John", 20)
    val studentInfo = with(student) {
        "Student's name is $name and age is $age"  // Uses properties of the object directly
    }
    println(studentInfo)  // Prints student's info without needing to reference 'student' explicitly
}
/*
Output:
Length of Alice is being calculated.
Length: 5
Name: Alice, Age: 31
Updated person: Person(name=Bob, age=31)
Result of sum: 15
Logging the number: 10
Original number: 10
Student's name is Alice and age is 20
 */

data class Person(var name: String, var age: Int)
data class Student(val name: String, val age: Int)
