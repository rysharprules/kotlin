package `03`.interfaces

interface Clickable3 {
    fun click()
    fun showOff() = println("I'm clickable!")  }

interface Focusable {
    fun setFocus(b: Boolean) =
        println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}

class Button2 : Clickable3, Focusable {
    override fun click() = println("I was clicked")
    // compiler forces you to provide your own implementation
    override fun showOff() {
        // You must provide an explicit implementation if more than one implementation for the same member is inherited
        // otherwise you will get error: The class 'Button2' must override public open fun showOff()
        // because it inherits many implementations of it.
        super<Clickable3>.showOff()
        super<Focusable>.showOff()  }

    // If you only need to invoke one inherited implementation, you can use the expression body syntax
    // override fun showOff() = super<Clickable>.showOff()
}

fun main() {
    val button = Button2()
    button.showOff()
    // I'm clickable!
    // I'm focusable!
    button.setFocus(true)
    // I got focus.
    button.click()
    // I was clicked.
}