package `04`

data class Person(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?
)

class ContactListFilters {
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate(): (Person) -> Boolean { // Declares a function that returns a function
        val startsWithPrefix = { p: Person -> p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if (!onlyWithPhoneNumber) {
            return startsWithPrefix // Returns a variable of a function type
        }
        return { startsWithPrefix(it) && it.phoneNumber != null } // Returns a lambda from this function
    }
}

fun main() {
    val contacts = listOf(
        Person("Dmitry", "Jemerov", "123-4567"),
        Person("Svetlana", "Isakova", null)
    )
    val contactListFilters = ContactListFilters()
    with (contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    // Passes the function returned by getPredicate as an argument to filter
    println(contacts.filter(contactListFilters.getPredicate()))
    // [Person(firstName=Dmitry, lastName=Jemerov, phoneNumber=123-4567)]
}