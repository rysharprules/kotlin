package `07`.deconstructing

// You can declare componentN functions manually for a non-data class:
class Point1(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}

// For a data class, the compiler generates a componentN function for every
//property declared in the primary constructor.
data class Point2(val x: Int, val y: Int) {
}

fun main() {
    val p1 = Point1(10, 20)
    val (x1, y1) = p1 // Declares variables x1 and y1, initialized with components of p1
    println(x1) // 10
    println(y1) // 20

    val p2 = Point2(45, 75)
    val (x2, y2) = p2
    println(x2) // 45
    println(y2) // 75
}