package `03`.interfaces

sealed interface Toggleable {
    fun toggle()
}

class LightSwitch: Toggleable {
    override fun toggle() = println("Lights!")
}

class Camera: Toggleable {
    override fun toggle() = println("Camera!")
}