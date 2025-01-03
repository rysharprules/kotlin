package `03`.constructors

// 1. Arguments
// If the constructor of a superclass takes arguments, then the primary constructor of your class also needs to initialize them.

open class User4(val nickname: String) { /* ... */ }

class SocialUser(nickname: String) : User4(nickname) {
    /* ... */
}

// 2. No arguments
// If you don’t declare any constructors for a class, a default constructor without parameters that does nothing will be generated for you

/*
If you inherit from the Button class and don’t provide any constructors, you have to explicitly invoke the constructor
of the superclass even if it doesn’t have any parameters. That’s why you need empty parentheses after the name of
the superclass.
*/

open class Button // A default constructor without arguments is generated

class RadioButton: Button() {
    /* ... */
}