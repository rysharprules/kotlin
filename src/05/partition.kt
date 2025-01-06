package `05`

val canBeInClub27 = { p: Person -> p.age <= 27 }

fun main() {
    val people = listOf(
        Person("Alice", 26),
        Person("Bob", 29),
        Person("Carol", 31)
    )
    val comeIn = people.filter(canBeInClub27)
    val stayOut = people.filterNot(canBeInClub27)
    println(comeIn) // [Person(name=Alice, age=26)]
    println(stayOut) // [Person(name=Bob, age=29), Person(name=Carol, age=31)]

    val result = people.partition(canBeInClub27) // Pair<List<Person>, List<Person>>
    val (comeIn2, stayOut2) = people.partition(canBeInClub27)
    println(comeIn2) // [Person(name=Alice, age=26)]
    println(stayOut2) // [Person(name=Bob, age=29), Person(name=Carol, age=31)]

    val result2 = people.partition { it.age <= 27 }
    println(result2) // ([Person(name=Alice, age=26)], [Person(name=Bob, age=29), Person(name=Carol, age=31)])
    println(result2.first) // [Person(name=Alice, age=26)]
}