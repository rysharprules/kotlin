package `05`

fun main() {
    val people = listOf(
        Person("Joe", 22),
        Person("Mary", 31),
        Person("Jamie", 22)
    )
    val personToAge = people.associateWith { it.age }
    println(personToAge)
    // {Person(name=Joe, age=22)=22, Person(name=Mary, age =31)=31, Person(name=Jamie, age=22)=22}

    val ageToPerson = people.associateBy { it.age }
    println(ageToPerson)
    // {22=Person(name=Jamie, age=22), 31=Person(name=Mary , age=31)}
    // Joe and Jamie have the same age which is being used as a key, Jamie is the last one in the list, so only he shows up in the Map
}