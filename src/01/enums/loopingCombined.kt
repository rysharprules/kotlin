package `01`.enums

/*
Combine multiple values in the same branch if you separate them with commas.
 */

fun measureColor() = Color.ORANGE // as a stand-in for more complex measurement logic

fun getWarmthFromSensor(): String {
    val color = measureColor()
    return when(color) {
        Color.RED, Color.ORANGE, Color.YELLOW -> "warm (red = ${color.r})"
        Color.GREEN -> "neutral (green = ${color.g})"
        Color.BLUE, Color.INDIGO, Color.VIOLET -> "cold (blue = ${color.b})"
    }

}
fun main() {
    println(getWarmthFromSensor())
    // warm (red = 255)
}