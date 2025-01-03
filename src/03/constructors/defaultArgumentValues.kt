package `03`.constructors

class User3(
    val nickname: String,
    val isSubscribed: Boolean = true
)
// If all the constructor parameters have default values, the compiler generates an additional constructor without
// parameters that uses all the default values.

fun main() {
    val alice = User3("Alice") // Uses the default value true for the isSubscribed parameter
    println(alice.isSubscribed)
    // true
    val bob = User3("Bob", false) // You can specify all parameters according to declaration order
    println(bob.isSubscribed)
    // false
    val carol = User3("Carol", isSubscribed = false) // You can explicitly specify names for some constructor arguments
    println(carol.isSubscribed)
    // false
    val dave = User3(nickname = "Dave", isSubscribed = true) // You can specify names for all constructor arguments
    println(dave.isSubscribed)
    // true
}