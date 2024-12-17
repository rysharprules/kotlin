package `01`.smartcast

/*
Sum(Sum(Num(1), Num(2)), Num(4))
(1 + 2) + 4

Tree-like structure:

                Sum
            /left \right
        Sum         Num(4)
     /left \right
    Num(1)  Num(2)

 */

interface Expr // marker interface
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int {
    if (e is Num) {
        val n = e as Num // explicit cast, not required as we can use 'e'
        return n.value
    }
    if (e is Sum) {
        return eval(e.right) + eval(e.left)  }
    throw IllegalArgumentException("Unknown expression")
}

// if is an expression in Kotlin so return statement and function curly braces are not required
fun eval_refactored(e: Expr): Int =
    if (e is Num) {
        e.value
    } else if (e is Sum) {
        eval_refactored(e.right) + eval_refactored(e.left)
    } else {
        throw IllegalArgumentException("Unknown expression")
    }

// curly braces are optional if there's only one expression in an if branch
fun eval_refactored2(e: Expr): Int =
    if (e is Num) e.value
    else if (e is Sum) eval_refactored2(e.right) + eval_refactored2(e.left)
    else throw IllegalArgumentException("Unknown expression")

// when would be better than if for expressing multiple choices
fun eval_refactored2_with_when(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval_refactored2_with_when(e.right) + eval_refactored2_with_when(e.left)
        else -> throw IllegalArgumentException("Unknown expression")
    }

fun main() {
    println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))
    // 7
    println(eval_refactored(Sum(Num(1), Num(2))))
    // 3
    println(eval_refactored2(Sum(Num(1), Num(2))))
    // 3
    println(eval_refactored2_with_when(Sum(Num(1), Num(2))))
    // 3
}