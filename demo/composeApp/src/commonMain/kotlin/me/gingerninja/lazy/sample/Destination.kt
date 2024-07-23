package me.gingerninja.lazy.sample

/*@Composable
fun rememberAppNavController(): AppNavController {
    return remember {
        AppNavController()
    }
}

class AppNavController {
    var currentScreen by mutableStateOf<DestinationId?>(null)
        private set

    val canGoBack by derivedStateOf { currentScreen != null }

    fun navigate(destination: DestinationId) {
        currentScreen = destination
    }

    fun back() {
        currentScreen = null
    }
}*/

sealed class Destination(
    val route: String,
    val title: String,
    val description: String? = null
) {
    data object Home : Destination(
        route = "home",
        title = "Lazy Sticky Headers",
    )

    data object ListVertical : Destination(
        route = "list/vertical",
        title = "LazyColumn",
        description = "Items are placed in a LazyColumn"
    )

    data object ListHorizontal : Destination(
        route = "list/horizontal",
        title = "LazyRow",
        description = "Items are placed in a LazyRow"
    )

    data object ListCalendar : Destination(
        route = "list/calendar",
        title = "Calendar",
        description = "Sample calendar schedule view"
    )
}

/*enum class Destination(val route: String) {
    HOME("home"),
    LIST_VERTICAL("list/vertical"),
    LIST_HORIZONTAL("list/horizontal"),
    LIST_CALENDAR("list/calendar")
}*/

val destinations = listOf(
    Destination.ListVertical,
    Destination.ListHorizontal,
    Destination.ListCalendar
)