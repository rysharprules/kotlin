package `05`

fun main() {
    val people = listOf(
        Person("Alex", 29),
        Person("Natalia", 28)
    )
    println(people.runningFold("") { acc, person ->
        acc + person.name
    })
    // [, Alex, AlexNatalia]
}