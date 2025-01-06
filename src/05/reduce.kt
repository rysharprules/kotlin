package `05`

fun main() {
    val list = listOf(1, 2, 3, 4)
    val summed = list.reduce { acc, element -> // you start with the first element of your collection in the accumulator (so don’t call it on an empty collection!).
        acc + element
    }
    println(summed) // 10

    val multiplied = list.reduce { acc, element ->
        acc * element
    }
    println(multiplied) // 24
}