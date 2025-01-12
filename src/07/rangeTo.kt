package `07`

import java.time.LocalDate

/*
The library defines the rangeTo function that can be called on any comparable element:

operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange<T>
 */
fun main() {
    val now = LocalDate.now()
    val vacation = now..now.plusDays(10) // Creates a 10-day range starting from now
    // Checks whether a specific date belongs to a range
    println(now.plusWeeks(1) in vacation) // true

    // The rangeTo operator has lower priority than arithmetic operators. But it’s
    // better to use parentheses for its arguments to avoid confusion
    val n = 9
    println(0..(n + 1)) // 0..10

    // Note 0..n.forEach {} won’t compile because you must surround a range expression with parentheses
    // to call a method on it:
    (0..n).forEach { print(it) } // 0123456789

    // the rangeUntil operator ( ..< ) returns an open-end range, which doesn’t include the specified upper bound:
    (0..<9).forEach { print(it) } // 012345678
}