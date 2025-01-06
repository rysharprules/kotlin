package `05`

fun main() {
    val people = listOf(Person("Joe", 22), Person("Mary", 31))
    val nameToAge = people.associate { it.name to it.age }
    println(nameToAge) // {Joe=22, Mary=31}
    println(nameToAge["Joe"]) // 22
}