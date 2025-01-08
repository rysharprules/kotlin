package `06`

class MyService {
    fun performAction(): String = "Action Done!"
}
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyTest {
    private lateinit var myService: MyService // Declares a property of a non-null type without an initializer

    // Initializes the property in the setUp method
    @BeforeAll fun setUp() {
        myService = MyService() // initialize the service
    }

    @Test fun testAction() {
        // Accesses the property without extra null checks
        assertEquals("Action Done!", myService.performAction())
    }
}