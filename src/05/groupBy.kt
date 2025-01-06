package `05`

fun main() {
    val people = listOf(
        Person("Alice", 31),
        Person("Bob", 29),
        Person("Carol", 31)
    )
    val result = people.groupBy { it.age } // Map<Int, List<Person>>
    println(result)
    // {31=[Person(name=Alice, age=31), Person(name=Carol, age=31)], 29=[Person(name=Bob, age=29)]}

    val list = listOf("apple", "apricot", "banana", "cantaloupe")
    println(list.groupBy(String::first)) // {a=[apple, apricot], b=[banana], c=[cantaloupe]}
}