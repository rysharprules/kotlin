package `06`

class Person1(val firstName: String, val lastName: String)
{
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Person1 ?: return false

        return otherPerson.firstName == firstName &&
         otherPerson.lastName == lastName
    }
    override fun hashCode(): Int =
        firstName.hashCode() * 37 + lastName.hashCode()
}
fun main() {
    val p1 = Person1("Dmitry", "Jemerov")
    val p2 = Person1("Dmitry", "Jemerov")
    println(p1 == null) // false
    println(p1 == p2) // true
    println(p1.equals(42)) // false
}