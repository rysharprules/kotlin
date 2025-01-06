package `05`

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val result = people.filter { it.age > 30 } // List<Person>
    println(result) // [Person(name=Bob, age=31)]

    val numbersMap = mapOf(0 to "zero", 1 to "one")
    val result2 = numbersMap.filterValues { it == "zero" }
    println(result2) // {0=zero}
    println(numbersMap.filterKeys { it == 0 }) // {0=zero}
}