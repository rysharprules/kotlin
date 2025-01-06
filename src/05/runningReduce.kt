package `05`

fun main() {
    val list = listOf(1, 2, 3, 4)
    val summed = list.runningReduce { acc, element ->
        acc + element
    }
    println(summed) // [1, 3, 6, 10]
    val multiplied = list.runningReduce { acc, element ->
        acc * element
    }
    println(multiplied) // [1, 2, 6, 24]
}