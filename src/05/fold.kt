package `05`

fun main() {
    val people = listOf(
        Person("Alex", 29),
        Person("Natalia", 28)
    )
    val folded = people.fold("") { acc, person -> // "" is the start value
        acc + person.name // "" + "Alex" on first pass, "Alex" + "Natalia" on second pass
    }
    println(folded)
}
// AlexNatalia