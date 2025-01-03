package `03`.interfaces

interface User {
    val nickname: String
}

/*
For PrivateUser, you use the concise syntax to declare a property directly in the primary constructor.
This property implements the abstract property from User, so you mark it as override.
 */
class PrivateUser(override val nickname: String) : User // Primary constructor property

/*
For SubscribingUser, the nickname property is implemented through a custom getter.
This property doesn’t have a backing field to store its value; it only has a getter that calculates a nickname from the email on every invocation.
 */
class SubscribingUser(val email: String) : User {
    override val nickname: String
        get() = email.substringBefore('@') // Custom getter
}

/*
For SocialUser, you assign the value to the nickname property in its initializer.
 */
class SocialUser(val accountId: Int) : User {
    override val nickname = getNameFromSocialNetwork(accountId) // Property initializer
}

fun getNameFromSocialNetwork(accountId: Int) =
    "kodee$accountId"

fun main() {
    println(PrivateUser("kodee").nickname)
    // kodee
    println(SubscribingUser("test@kotlinlang.org").nickname)
    // test
    println(SocialUser(123).nickname)
    // kodee123
}