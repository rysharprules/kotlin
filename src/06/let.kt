package `06`

fun sendEmailTo(email: String) {
    println("Sending email to $email")
}
fun main() {
    var email: String? = "yole@example.com"
    email?.let { sendEmailTo(it) }
    // Sending email to yole@example.com
    email = null
    email?.let { sendEmailTo(it) }
}