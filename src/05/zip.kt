package `05`

fun main() {
    val names = listOf("Joe", "Mary", "Jamie")
    val ages = listOf(22, 31, 31, 44, 0)
    println(names.zip(ages))
    // [(Joe, 22), (Mary, 31), (Jamie, 31)]
     println(names.zip(ages) { name, age -> Person(name, age) })
    // [Person(name=Joe, age=22), Person(name=Mary, age=31), Person(name=Jamie, age=31)]

    println(names zip ages)
    // [(Joe, 22), (Mary, 31), (Jamie, 31)]

    val countries = listOf("DE", "NL", "US")
    // Like the to function to create Pair objects, the zip function can also be
    //called as an infix function
    println(names zip ages zip countries)
    // Combining multiple calls to zip results in a list of nested pairs. Note the extra
    //parentheses around the names and ages.
    // [((Joe, 22), DE), ((Mary, 31), NL), ((Jamie, 31), US)]
}