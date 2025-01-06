package `05`

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val result = people.map { it.name }  // type is now List<String>
    // same as above with method reference
    println(Person::name)  // [Alice, Bob]

    println(people.filter { it.age > 30 }.map(Person::name)) // [Bob]

    val numbersMap = mapOf(0 to "zero", 1 to "one")
    println(numbersMap.mapKeys { it.key + 1 }) // {1=zero, 2=one}
    println(numbersMap.mapValues { it.value.uppercase() }) // {0=ZERO, 1=ONE}
}