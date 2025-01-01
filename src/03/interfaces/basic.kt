package `03`.interfaces

interface Clickable1 {
    fun click()
}

class Button : Clickable1 {
    override fun click() = println("I was clicked")
}

fun main() {
    Button().click()
    // I was clicked
}