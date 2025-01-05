# Lambdas

Lambdas can remove redundant code.
```kotlin
button.setOnClickListener(object: OnClickListener {
    override fun onClick(v: View) {
        println("I was clicked!")
    }
})
```

```kotlin
button.setOnClickListener {
    println("I was clicked!")
}
```

**Syntax for lambda:**
- Always in curly braces `{ ... }`
- Parameters are on the left side of `->` -- `{ x: Int, y: Int -> ... }`
- Body is on the right side of `->` -- `{ ... -> x + y }`

## Variables
You can store a lambda in a variable.
```kotlin
val sum = { x: Int, y: Int -> x + y }
```

## Scope
Lambdas can access variables defined in the surrounding scope — all the way up to the surrounding class and file scopes.

<img src=img/04_lambda_scope.png width=490 height=210>

Kotlin allows you to access (and modify) non-final variables in a lambda. External variables accessed from a lambda, 
such as `prefix` in the example above, is said to be _captured_ by the lambda.

## Member references
**Syntax for member reference:**
- On the left hand side is the class or _bound callabale reference_ `MyClass`
- In the middle is the `::` operator
- On the right hand side is the member function `::myFunction`

Example: `{ person: Person -> person.age }` can become `Person::age`

### Bound callable reference
```kotlin
data class Person(val name: String, val age: Int)

fun main() {
    val seb = Person("Sebastian", 26)
    val personsAgeFunction = Person::age 
    println(personsAgeFunction(seb))  // 26
    val sebsAgeFunction = seb::age 
    println(sebsAgeFunction())  // 26
}
```

### Constructor reference
You can store or postpone the action of creating an instance of a class using a constructor reference
```kotlin
data class Person(val name: String, val age: Int)

fun main() {
    val createPerson = ::Person 
    val p = createPerson("Alice", 29)
    println(p)
    // Person(name=Alice, age=29)
}
```

### Top-level function reference 
You can have a reference to a function that’s declared at the top level (and isn’t a member of a class):
```kotlin
fun salute() = println("Salute!")
fun main() {
    run(::salute) // Salute!

    // When a lambda delegates to a function that takes several parameters, it’s
    // especially convenient to provide a member reference
    val action = { person: Person, message: String -> 
        sendEmail(person, message)
    }
    val nextAction = ::sendEmail
}
```

## Single Abstract Method (SAM) interfaces

```kotlin
fun interface IntCondition {
    fun check(i: Int): Boolean // Exactly one abstract method
    // non-abstract methods
    fun checkString(s: String) = check(s.toInt())
    fun checkChar(c: Char) = check(c.digitToInt()) 
}
fun main() {
    val isOdd = IntCondition { it % 2 != 0 }
    println(isOdd.check(1)) // true
    println(isOdd.checkString("2")) // false
    println(isOdd.checkChar('3')) // true
}
```

```kotlin
fun checkCondition(i: Int, condition: IntCondition): Boolean {
    return condition.check(i)
}
fun main() {
    // Either pass a lambda directly
    checkCondition(1) { it % 2 != 0 }  // true
    // or reference a lambda with a matching signature.
    val isOdd: (Int) -> Boolean = { it % 2 != 0 }
    checkCondition(1, isOdd)  // true
}
```

## Lambdas with receivers: `with`, `apply`, `also`

### with
With `with` you can call multiple methods on an object instance.
The `with` structure looks like a special construct, but it’s a function that takes two arguments, below: `myTurtle` and a lambda.
```kotlin
class Turtle {
    fun penDown()
    fun penUp()
    fun turn(degrees: Double)
    fun forward(pixels: Double)
}

val myTurtle = Turtle()
with(myTurtle) { //draw a 100 pix square
    // you can use this here, but it's not necessary
    //this.penDown()
    penDown()
    for (i in 1..4) {
        forward(100.0)
        turn(90.0)
    }
    penUp()
}
```
The convention of putting the lambda outside the parentheses works here, and the entire invocation looks like a built-in feature of the language.
Alternatively, you could write this as `with(myTurtle, { ... })`, but it’s less readable.

### apply
The apply function works almost exactly the same as `with`; the main difference is that apply always returns the object 
passed to it as an argument (in other words, the receiver object).

This is useful for configuring properties that aren't present in the object constructor.
```kotlin
val myRectangle = Rectangle().apply {
    length = 4
    breadth = 5
    color = 0xFAFAFA
}

// creates an Android TextView component with some custom attributes
fun createViewWithCustomAttributes(context: Context) =
    TextView(context).apply {
        text = "Sample Text"
        textSize = 20.0
        setPadding(10, 0, 0, 0) 
    }
```

### also
Like `apply`, you can use the `also` function to take a receiver object, perform an action on it, and then return the receiver object.
Within the lambda of `also`, you access the receiver object as an argument—either by giving it a name, or using the default
name `it`.

## buildXXX functions
`buildString` is a lambda with a receiver, and the receiver is always a `StringBuilder`
```kotlin
fun alphabet() = buildString {
    for (letter in 'A'..'Z') {
    append(letter)
    }
    append("\nNow I know the alphabet!")
}
```

You can use `buildList`, `buildSet`, and `buildMap` to create collections

```kotlin
val fibonacci = buildList {
    addAll(listOf(1, 1, 2))
    add(3)
    add(index = 0, element = 3)
}

val shouldAdd = true
val fruits = buildSet {
    add("Apple")
    if (shouldAdd) {
        addAll(listOf("Apple", "Banana", "Cherry"))
    }
}

val medals = buildMap { // buildMap<String, Int> implied
    put("Gold", 1)
    putAll(listOf("Silver" to 2, "Bronze" to 3))
}
```