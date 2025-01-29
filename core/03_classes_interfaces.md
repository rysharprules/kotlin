# Classes, objects and interfaces

- There is no package private in Kotlin
- In Kotlin, `==` checks whether the objects are equal, not the references. It is
  compiled to a call of equals. For reference comparison, use the `===` operator

## Constructors

The `constructor` keyword begins the declaration of a primary or secondary constructor
The `init` keyword introduces an initializer block, executed when the class is created
and are intended to be used together with primary constructors

**Examples**
- [Primary constructor and init block](../src/03/constructors/init.kt)
- [Superclasses with and without arguments](../src/03/constructors/superclass.kt)
- [Private constructor](../src/03/constructors/private.kt)
- [Constructor with arguments with default values](../src/03/constructors/defaultArgumentValues.kt)

### Secondary (multiple) constructors

Less common than in Java. Most situations where you’d need overloaded constructors are covered 
by Kotlin’s support for default parameter values and named argument syntax.

Subclasses can use `super` and `this` to delegate.

If the class has no primary constructor, then each secondary constructor must initialize the 
base class or delegate to another constructor that does so. Thinking in terms of the diagrams
below, each secondary constructor must have an outgoing arrow starting a path that ends at 
any constructor of the base class. 

This is demonstrated in the [Example code](../src/03/constructors/secondary.kt).

<img src=../img/core/03/secondary_constructor_super.png width="600" height="180">

<img src=../img/core/03/secondary_constructor_this.png width="600" height="180">

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
- [Basic interface](../src/03/interfaces/basic.kt)
- [Explicit implementations](../src/03/interfaces/explicitImplementations.kt)

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
- [Default implementation](../src/03/interfaces/defaultImplementations.kt)

### Properties declared in interfaces

An `interface` can contain abstract property declarations which need to be overridden in implementing classes.


```kotlin
interface User {
    val nickname: String
}
```
Classes implementing the `User` interface above need to provide a way to obtain the value of 
`nickname`. 

The interface doesn’t specify whether the value should be stored in a backing field or obtained through a getter.
See [examples of interface properties overridden](../src/03/interfaces/properties.kt).

The interface _can_ contain properties with getters and setters, as long as they don't reference a backing field.

```kotlin
interface EmailUser {
    val email: String
    val nickname: String
        get() = email.substringBefore('@') // The property doesn’t have a backing field: the result value is computed on each access.  
}
```

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

You can override these:
```kotlin
class Customer(val name: String, val postalCode: Int) {
    override fun toString() = "Customer(name=$name, postalCode=$postalCode)"
}
```
**Examples**
- [Overriding generated methods](../src/03/dto.kt)
- [The copy method](../src/03/copy.kt) - a method that allows you to copy the instances of your classes, changing the values of some properties

### `==` and `equals`

The comparison `a == b` checks whether `a` isn’t null , and if it’s not, calls `a.equals(b)`- otherwise, the result is true only if both arguments are null
references.

<img src=../img/core/03/equals.png width="390" height="30">

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

The same rules apply for interfaces. [Example sealed interface](../src/03/interfaces/sealed.kt)

## Class delegation

If you need to add behavior to another class, even if it wasn’t designed to be extended, you may use the decorator pattern.
This often requires a large amount of boilerplate code:

```kotlin
class DelegatingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>): Boolean =innerList.containsAll(elements)
}
```

Use the `by` keyword to delegate the implementation of an interface to another object. All the method 
implementations in the class are gone. The compiler will generate them:

```kotlin
class DelegatingCollection<T>(
    innerList: Collection<T> = mutableListOf<T>()) : Collection<T> by innerList
```

When you need to change the behavior of some methods, you can override them, and your code will be called 
instead of the generated methods. [See example](../src/03/by.kt)

## Objects

When you want a class for which you need only one instance, you can use the `object` keyword. Rather than the Singleton pattern.

The object declaration combines a class declaration and a declaration of a single instance of that class.

Unlike instances of regular classes, object declarations are created immediately at the point of definition, not through constructor calls from other places in the code.
```kotlin
object Payroll {
  val allEmployees = mutableListOf<Person>()
  fun calculateSalary() {
    for (person in allEmployees) {
      /* ... */
    }
  }
}

fun main() {
  Payroll.allEmployees.add(Person("Alice"))
  Payroll.calculateSalary()
}
```
Examples:
- [Object in a class](../src/03/objects/objectInClass.kt)
- [Comparator example](../src/03/objects/comparator.kt)
- [Comparator in a class](../src/03/objects/comparatorInClass.kt)

### Companion objects

Similar to `static` members in Java, but they can also implement interfaces. 

Examples:
- [Basic companion object](../src/03/objects/companion.kt)
- [Companion object used for factory pattern](../src/03/objects/companionFactory.kt)
