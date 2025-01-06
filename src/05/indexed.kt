package `05`

fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7)
    val filtered = numbers.filterIndexed { index, element ->
        // filtering a list of numbers to only contain those values at an even index
        //and greater than 3
        index % 2 == 0 && element > 3
    }
    println(filtered) // [5, 7]
    val mapped = numbers.mapIndexed { index, element ->
        index + element
    }
    println(mapped) // [1, 3, 5, 7, 9, 11, 13]
}