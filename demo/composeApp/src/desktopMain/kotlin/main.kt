import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.gingerninja.lazy.sample.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Lazy Sticky Headers demo",
    ) {
        App()
    }
}