# Classes, objects and interfaces

- There is no package private in Kotlin

## Interfaces

`override` modifier is mandatory
```kotlin
interface Clickable {
    fun click()
}

class Button : Clickable {
    override fun click() = println("I was clicked")
}

fun main() {
    Button().click()
    // I was clicked
}
```

**Examples**
- [Basic interface](src/03/interfaces/basic.kt)
- [Explicit implementations](src/03/interfaces/explicitImplementations.kt)

### Default implementation

```kotlin
interface Clickable {
    fun click() 
    fun showOff() = println("I'm clickable!") 
}
```
If you implement this interface, you are forced to provide an implementation
for `click`. You can redefine the behavior of the `showOff` method or use the default behaviour.

**Examples**
- [Default implementation](src/03/interfaces/defaultImplementations.kt)

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

## Open and abstract classes

You can’t create a subclass for a Kotlin class or override any methods from a base class — all classes and methods are `final`, by default.
```kotlin
open class RichButton : Clickable { // This class is open: others can inherit from it.
    fun disable() { /* ... */ } // This function is final: you can’t override it in a subclass.
    open fun animate() { /* ... */ } // This function is open: you may override it in a subclass.
    override fun click() { /* ... */ }  } // This function overrides an open function and is open as well.

class ThemedButton : RichButton() {
    // disable is final in RichButton by default, you can’t override it here
    override fun animate() { /* ... */ } // animate is explicitly open, so you can override it.
    override fun click() { /* ... */ } // You can override click from Clickable interface because RichButton didn’t explicitly mark it as final
    override fun showOff() { /* ... */ }  } // You can override showOff even though RichButton didn’t provide an override.
```

### Abstract classes
Abstract members are always open, so you don’t need to use an explicit `open` modifier (just like
you don’t need an explicit `open` modifier in an `interface`).

```kotlin
abstract class MyAbstractClass {
    abstract val field1 : Int // Doesn’t have a value, and subclasses need to override its value or accessor.
    val field2 : Double = 2.2 // Properties in abstract classes aren’t open by default but can be explicitly marked as open.
    // open val field2 : Double = 2.2
    abstract fun doSomething()
    abstract fun sleep()
}
```

### Modifiers summary

| Modifier  | Corresponding Member                  | Comments                                                                 |
|-----------|---------------------------------------|--------------------------------------------------------------------------|
| `final`   | Can’t be overridden                   | Used by default for class members                                         |
| `open`    | Can be overridden                     | Should be specified explicitly                                             |
| `abstract`| Must be overridden                    | Can be used only in abstract classes; abstract members can’t have an implementation |
| `override`| Overrides a member in a superclass or interface | Overridden member is open by default, if not marked final                  |

### Visibility modifiers

| Modifier   | Class Member                    | Top-level Declaration           |
|------------|----------------------------------|---------------------------------|
| `public` (default) | Visible everywhere             | Visible everywhere              |
| `internal` | Visible in a module              | Visible in a module             |
| `protected`| Visible in subclasses            | —                               |
| `private`  | Visible in a class               | Visible in a file               |

## Inner and nested classes

A nested class in Kotlin with no explicit modifiers is the same as a `static`
nested class in Java.

```kotlin
class Button : View {
    override fun getCurrentState(): State = ButtonState()
    override fun restoreState(state: State) { /*...*/ }
    class ButtonState : State { /*...*/ }  // This class is an analogue of a static nested class in Java.
}
```

To turn it into an inner class so that it contains a reference
to an outer class, you use the `inner` modifier

Nested classes don’t reference their outer class, whereas inner
classes do:
<img src=img/03_nested_class_this.png width="600" height="180">
```kotlin
class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer
    }
}
```

**Correspondence between nested and inner classes in Java and Kotlin**

| **Class A declared within another class B** | **In Java**         | **In Kotlin**      |
|----------------------------------------------|---------------------|--------------------|
| **Nested class (doesn’t store a reference to an outer class)** | static class A       | class A            |
| **Inner class (stores a reference to an outer class)**      | class A             | class A inner      |

## Sealed classes and interfaces

All direct subclasses of a `sealed` class must:
- be known at compile time 
- declared in the same package as the `sealed` class itself
- all subclasses need to be located within the same module

sealed modifier implies that the class is `abstract`; you don’t need an explicit `abstract` modifier and can
declare `abstract` members.

```kotlin
sealed class Expr 
class Num(val value: Int) : Expr()
class Sum(val left: Expr, val right: Expr) : Expr()

fun eval(e: Expr): Int =
    // If you handle all subclasses of a sealed class in a when expression, you
    // don’t need to provide the default branch—the compiler can ensure you’ve
    // covered all possible branches.
    when (e) { 
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
    }
```

The same rules apply for interfaces. [Example sealed interface](src/03/interfaces/sealed.kt)

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