import `03`.interfaces.getNameFromSocialNetwork

// private constructor
class User private constructor(val nickname: String) {
     companion object {
        fun newSubscribingUser(email: String) = User(email.substringBefore('@'))
        fun newSocialUser(accountId: Int) = User(getNameFromSocialNetwork(accountId))
    }
}

fun main() {
    val subscribingUser = User.newSubscribingUser("bob@gmail.com")
    val socialUser = User.newSocialUser(4)
    println(subscribingUser.nickname)
    // bob
}