/*
 * Copyright 2024 Gergely Kőrössy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.gingerninja.lazy.sample.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.gingerninja.lazy.sample.Destination
import me.gingerninja.lazy.sample.ui.component.NavScreen

fun NavGraphBuilder.listScreens(
    onBack: () -> Unit,
    onScreenClick: (destination: Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    navigation(
        startDestination = "list/root",
        route = ListDestinations.root.route,
    ) {
        composable("list/root") {
            ListScreen(
                onBack = onBack,
                onScreenClick = onScreenClick,
                modifier = modifier,
            )
        }

        composable(ListDestinations.Contact.route) {
            ContactListScreen(
                modifier = modifier,
                onBack = onBack,
            )
        }

        composable(ListDestinations.CalendarRow.route) {
            CalendarRowListScreen(
                modifier = modifier,
                onBack = onBack,
            )
        }

        composable(ListDestinations.Calendar.route) {
            CalendarListScreen(
                onBack = onBack,
                modifier = modifier,
            )
        }
    }
}

object ListDestinations {
    val root = Destination(
        route = "list",
        title = "Lists",
        description = "LazyColumn and LazyRow examples",
    )
    internal val Contact = Destination(
        route = "list/contact",
        title = "Simple LazyColumn",
        description = "A vertical list showcasing a simple contact list",
    )

    internal val CalendarRow = Destination(
        route = "list/calendar-row",
        title = "Simple LazyRow",
        description = "A horizontal list showcasing a calendar row",
    )

    internal val Calendar = Destination(
        route = "list/calendar",
        title = "Complex calendar",
        description = "Sample calendar schedule view",
    )
}

private val destinations = listOf(
    ListDestinations.Contact,
    ListDestinations.CalendarRow,
    ListDestinations.Calendar,
)

@Composable
private fun ListScreen(
    onBack: () -> Unit,
    onScreenClick: (destination: Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavScreen(
        title = ListDestinations.root.title,
        subtitle = ListDestinations.root.description,
        destinations = destinations,
        onScreenClick = onScreenClick,
        modifier = modifier,
        onBack = onBack,
    )
}
