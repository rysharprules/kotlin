package `03`.constructors

class User1(val nickname: String)

class User2 constructor(_nickname: String) { // Primary constructor with one parameter
    val nickname: String

    init { // Initializer block
        nickname = _nickname // _nickname used instead of this.nickname = nickname
    }
}