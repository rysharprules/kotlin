package `03`.objects

class MyClass {
    companion object {
        fun callMe() {
            println("Companion object called")
        }
    }
}

fun main() {
    MyClass.callMe()
    // Companion object called

    val myObject = MyClass()
    //myObject.callMe()  // Error: Unresolved reference: callMe
}