package `01`.range

/*
If i is divisible by 15, it returns FizzBuzz. % is the remainder operator.
If i is divisible by 3, it returns Fizz.
If i is divisible by 5, it returns Buzz.
else returns the number itself.

Iterates over the integer range 1..100
 */
fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz "
    i % 3 == 0 -> "Fizz "
    i % 5 == 0 -> "Buzz "
    else -> "$i "
}

fun main() {
    for (i in 1..100) {
        print(fizzBuzz(i))
    }
    // 1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz 16 17 Fizz 19 Buzz Fizz 22 23 Fizz Buzz 26 ...
}