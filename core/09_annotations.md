# Annotations
Annotations allow you to associate additional metadata with a declaration

**Examples**

`@Test` annotation instructs the framework to invoke this method as a test
```kotlin
import kotlin.test.*
class MyTest {
    @Test 
    fun testTrue() {
        assertTrue(1 + 1 == 2)
    }
}
```
`@Deprecated` annotation takes up to three parameters: message, replaceWith, and level
```kotlin
@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int) { /* ... */ }
```

## Use-site targets

The use-site target (like `get` or `set`) is placed between the @ sign and the annotation name, and is separated 
from the name with a colon.

<img src=../img/core/09/usesite.png width=380 height=110>

Here, you use it to make the calculate function callable from Java code via `performCalculation()`:
```kotlin
@JvmName("performCalculation")
fun calculate(): Int {
    return (2 + 2) - 1
}
```
Explicitly apply the `@JvmName` annotation to the getter or setter of a property:
```kotlin
class CertificateManager { 
    @get:JvmName("obtainCertificate")
    @set:JvmName("putCertificate")
    var certificate: String = "-----BEGIN PRIVATE KEY-----"}
```

Supported use-site targets:

- `property` — Property (Java annotations can’t be applied with this use-site target)
- `field` — Field generated for the property
- `get` — Property getter
- `set` — Property setter
- `receiver` — Receiver parameter of an extension function or property
- `param` — Constructor parameter
- `setparam` — Property setter parameter
- `delegate` — Field storing the delegate instance for a delegated property
- `file` — Class containing top-level functions and properties declared in the file

## Controlling the Java API with annotations

- `@JvmName` changes the name of a Java method or field generated from a Kotlin declaration.
- `@JvmStatic` can be applied to methods of an object declaration or a companion object to expose them as 
`static` Java methods.
- `@JvmOverloads`, instructs the Kotlin compiler to generate overloads for a function or constructor 
that has default parameter values.
- `@JvmField` can be applied to a property to expose that property as a public Java field with no getters or setters.
- `@JvmRecord` can be applied to a data class to declare a Java record class

## JSON serialization with annotations

- `@JsonClass` annotation is used to specify the name of the generated JSON class
- `@JsonExclude` annotation is used to mark a property that should be excluded from serialization and 
deserialization.
- `@JsonName` annotation allows you to specify that the key in the key-value pair representing the property 
should be the given string, not the name of the property.

```kotlin
data class Person(
    @JsonName("alias") val firstName: String,
    @JsonExclude val age: Int? = null
)
```

<img src=../img/core/09/json_ser.png width=380 height=110>

## Creating annotations

```kotlin
annotation class JsonExclude
annotation class JsonName(val name: String)
```

### Meta-annotations

- `@Target` specifies where the annotation can be used: `CLASS`, `PROPERTY`, `FIELD`, `LOCAL_VARIABLE`, `VALUE_PARAMETER`, `CONSTRUCTOR`, `FUNCTION`, `PROPERTY_GETTER`, `PROPERTY_SETTER`, `TYPE`, `EXPRESSION`, `FILE`, `TYPE_PARAMETER`, `VALUE_PARAMETER`, `ANNOTATION_CLASS`, `ANNOTATION_CLASS`, `TYPEALIAS`
```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude 
```
To declare your own meta-annotation, use `ANNOTATION_CLASS` as its target:
```kotlin
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class BindingAnnotation

@BindingAnnotation
annotation class MyBinding
```