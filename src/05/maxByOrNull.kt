package `05`

fun findTheOldestManually(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {  maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

fun main() {
    val people = listOf(
        Person("Alice", 29), Person("Bob",
        31)
    )
    findTheOldestManually(people)
    // Person(name=Bob, age=31)

    // You can use a function from the standard library
    println(people.maxByOrNull { it.age })
    // Person(name=Bob, age=31)

    // You can use a method reference
    println(people.maxByOrNull(Person::age))

    println(people.filter { it.age > 30 }.map(Person::name)) // [Bob]

    val maxAge = people.maxByOrNull(Person::age)?.age
    println(maxAge) // 31
    val result = people.filter { it.age == maxAge }
    println(result) // [Person(name=Bob, age=31)]
}