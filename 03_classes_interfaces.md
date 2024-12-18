# Classes, objects and interfaces


## DTOs (POJOs/POCOs)

[DTO - Data transfer object](https://en.wikipedia.org/wiki/Data_transfer_object)
[POJO - Plain old Java object](https://en.wikipedia.org/wiki/Plain_old_Java_object)
[POCO - Plain old CLR object](https://en.wikipedia.org/wiki/Plain_old_CLR_object)

```kotlin
data class Customer(val name: String, val email: String)
```
As a class getters (and setters in case of vars) for all properties are provided. 
Additionally, `data` provides:

* equals()
* hashCode()
* toString()
* copy()

## Singletons

```kotlin
object Resource {
    val name = "Name"
}
```

## Abstract classes
```kotlin
abstract class MyAbstractClass {
    abstract fun doSomething()
    abstract fun sleep()
}

fun main() {
    val myObject = object : MyAbstractClass() {
        override fun doSomething() {
            // ...
        }

        override fun sleep() { // ...
        }
    }
    myObject.doSomething()
}
```
## Objects

### apply
This is useful for configuring properties that aren't present in the object constructor.
```kotlin
val myRectangle = Rectangle().apply {
    length = 4
    breadth = 5
    color = 0xFAFAFA
}
```

### with
With `with` you can call multiple methods on an object instance
```kotlin
class Turtle {
    fun penDown()
    fun penUp()
    fun turn(degrees: Double)
    fun forward(pixels: Double)
}

val myTurtle = Turtle()
with(myTurtle) { //draw a 100 pix square
    penDown()
    for (i in 1..4) {
        forward(100.0)
        turn(90.0)
    }
    penUp()
}
```