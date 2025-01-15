package `04`

enum class Delivery { STANDARD, EXPEDITED }
class Order(val itemCount: Int)

// Declares a function that returns a function
fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {
    // Returns lambdas from the function
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}

fun main() {
    val calculator = getShippingCostCalculator(Delivery.EXPEDITED) // Stores the returned function in a variable
    // Invokes the returned function
    println("Shipping costs ${calculator(Order(3))}") // Shipping costs 12.3
}