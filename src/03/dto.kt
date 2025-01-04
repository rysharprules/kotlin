package `03`

class CustomerClass1(val name: String, val postalCode: Int) {
    // default toString would be something like 03.CustomerClass1@52cc8049
    override fun toString() = "Customer custom toString(name=$name, postalCode=$postalCode)"
}

data class CustomerDataClass1(val name: String, val postalCode: Int) {
}

class CustomerClass2(val name: String, val postalCode: Int) {

    override fun hashCode(): Int = name.hashCode() * 31 +
            postalCode

    override fun equals(other: Any?): Boolean {
         if (other == null || other !is CustomerClass2)
         return false
        return name == other.name &&
         postalCode == other.postalCode
    }
    override fun toString() = "Customer(name=$name, postalCode=$postalCode)"
}

fun main() {
    val customer1 = CustomerClass1("Alice", 342562)
    println(customer1) // Customer custom toString(name=Alice, postalCode=342562)
    val customer2 = CustomerDataClass1("Alice", 342562)
    // generated toString from data class
    println(customer2) // CustomerDataClass1(name=Alice, postalCode=342562)

    // equals is not overridden so reference is compared
    val customer3 = CustomerClass1("Alice", 342562)
    val customer4 = CustomerClass1("Alice", 342562)
    println(customer3 == customer4) // false

    // data classes include generated equals
    val customer5 = CustomerDataClass1("Alice", 342562)
    val customer6 = CustomerDataClass1("Alice", 342562)
    println(customer5 == customer6) // true

    val processed1 = hashSetOf(CustomerClass1("Alice", 342562))
    println(processed1.contains(CustomerClass1("Alice", 342562)))
    // false as hashcode and equals are not overridden
    val processed2 = hashSetOf(CustomerDataClass1("Alice", 342562))
    println(processed2.contains(CustomerDataClass1("Alice", 342562)))
    // true as hashcode and equals are overridden as this is a data class
    val processed3 = hashSetOf(CustomerClass2("Alice", 342562))
    println(processed3.contains(CustomerClass2("Alice", 342562)))
    // true as hashcode and equals are overridden manually
}