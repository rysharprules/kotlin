package `04`

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)
enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID)
)

// average duration of visits from Windows machines
val averageWindowsDuration = log
    .filter { it.os == OS.WINDOWS }
    .map(SiteVisit::duration)
    .average()

// calculate the same statistics for Mac users
fun List<SiteVisit>.averageDurationFor(os: OS) = filter { it.os == os }
     .map(SiteVisit::duration).average()

fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()


fun main() {
    // Windows
    println(averageWindowsDuration) // 23.0

    // OS
    println(log.averageDurationFor(OS.WINDOWS)) // 23.0
    println(log.averageDurationFor(OS.MAC)) // 22.0

    // mobile platforms
    val averageMobileDuration = log
            .filter { it.os in setOf(OS.IOS, OS.ANDROID) }
            .map(SiteVisit::duration)
            .average()
    println(averageMobileDuration) // 12.15

    // You can use function types to extract the required condition into a parameter.
    println(
        log.averageDurationFor {
            it.os in setOf(OS.ANDROID, OS.IOS)
        }
    ) // 12.15

    println(
        log.averageDurationFor {
            it.os == OS.IOS && it.path == "/signup" // “What’s the average duration of visits to the signup page from iOS?”
        }
    ) // 8.0
}