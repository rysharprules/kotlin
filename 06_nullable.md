# Nullable
- All regular types are non-nullable by default


## Nullable types
`fun strLen(s: String) = s.length`

Calling `strLen` with an argument that may be `null` isn’t allowed and will be flagged as an error at compile time:

```kotlin
fun main() {
    strLen(null) // ERROR: Null can not be a value of a nonnull type String
}
```

The parameter is declared as type `String`, and in Kotlin, this means it must always contain a `String` instance. The compiler enforces that, so you can’t
pass an argument containing `null`.

This gives you the guarantee that the `strLen` function will never throw a `NullPointerException` at run time.

To use this with `null` you need to mark this explicitly by putting a question mark after the type name:

`fun strLenSafe(s: String?) = ...`

The set of operations you can perform on a nullable type is restricted:

```kotlin
fun strLenSafe(s: String?) = s.length
// ERROR: only safe (?.) or nonnull asserted (!!.) calls are allowed on a nullable receiver of type kotlin.String?

val x: String? = null
strLen(x) // ERROR: Type mismatch: inferred type is String? but String was expected

val y: String? = null
var z: String = y // ERROR: Type mismatch: inferred type is String? but String was expected
```

Valid:
```kotlin
fun strLenSafe(s: String?): Int =
    if (s != null) s.length else 0 // By adding the check for null, the code now compiles.

fun main() {
    val x: String? = null
    println(strLenSafe(x)) // 0
    println(strLenSafe("abc")) // 3
}
```

## Safe call operator `?.`
```kotlin
str?.uppercase() // the result of .uppercase() is String, but the result of str?.uppercase() is String?
// the above is equivalent to the following
if (str != null) str.uppercase() else null
```
<img src=img/06_safecall_operator.png width=590 height=200>

## Elvis operator `?:`
```kotlin
fun greet(name: String?) {
    // If name is null, recipient will be set to unnamed instead.
    val recipient: String = name ?: "unnamed" 
    println("Hello, $recipient!")
}
```
<img src=img/06_elvis_operator.png width=590 height=200>

Elvis operator returns an expression which includes `return` and `throw`. 

[Elvis examples](src/06/elvis.kt)

## Safe casting operator `as?`
```kotlin
class Person(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Person ?: return false
        return otherPerson.firstName == firstName &&
            otherPerson.lastName == lastName
```
[Full example here](src/06/safe_casting.kt)

<img src=img/06_safecast_as.png width=590 height=200>

## Not-null assertion operator `!!`
You don’t have to explicitly handle `null`
```kotlin
fun ignoreNulls(str: String?) {
    val strNotNull: String = str!!
    println(strNotNull.length)
}
fun main() {
    ignoreNulls(null) // Exception in thread "main" java.lang.NullPointerException at <...>.ignoreNulls(....kt:2)
}
```

## Let function
```kotlin
if (email != null) sendEmailTo(email)
// or alternatively use let
email?.let { email -> sendEmailTo(email) }
// or even shorter using it
email?.let { sendEmailTo(it) }
```
[Full example here](src/06/let.kt)

<img src=img/06_let.png width=590 height=200>

## `lateinit`
Declare a property of a non-null type without an initializer, i.e. initilize it later.
```kotlin
class MyTest {
    private lateinit var myService: MyService
    
    @BeforeAll fun setUp() {
        myService = MyService()
    }
    @Test fun testAction() {
        assertEquals("Action Done!", myService.performAction())
    }
```
[Full example here](src/06/lateinit.kt)

<img src=img/06_lateinit.png width=590 height=200>

## Extensions for nullable types
Defining extension functions for nullable types is one more powerful way to deal with `null` values. 
Rather than ensuring a variable can’t be `null` before a method call, you can allow the calls with 
`null` as a receiver and deal with `null` in the function.

```kotlin
fun String?.isNullOrBlank(): Boolean = 
    this == null || this.isBlank()

fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) { // No safe call is needed.
        println("Please fill in the required fields")
    }
}
fun main() {
    verifyUserInput(" ") // Please fill in the required fields
    
    // No exception happens when you call isNullOrBlank with null as a receiver.
    verifyUserInput(null) // Please fill in the required fields
}
```
<img src=img/06_extension_nullable.png width=260 height=135>

This is only possible for extension functions; regular member calls are dispatched through the object 
instance and, therefore, can never be performed when the instance is `null`.