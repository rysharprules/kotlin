package `03`

data class Customer(val name: String, val postalCode: Int) {
}

fun main() {
    val bob = Customer("Bob", 973293)
    println(bob.copy(postalCode = 382555))
    // Customer(name=Bob, postalCode=382555)
}