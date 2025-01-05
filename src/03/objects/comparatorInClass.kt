package `03`.objects

data class Person2(val name: String) {
    object NameComparator : Comparator<Person2> {
        override fun compare(p1: Person2, p2: Person2): Int = p1.name.compareTo(p2.name)
    }
}
fun main() {
    val persons = listOf(Person2("Bob"), Person2("Alice"))
    println(persons.sortedWith(Person2.NameComparator))
    // [Person2(name=Alice), Person(name=Bob)]
}