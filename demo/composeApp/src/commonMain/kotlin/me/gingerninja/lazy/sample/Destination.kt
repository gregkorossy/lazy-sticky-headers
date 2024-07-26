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
package me.gingerninja.lazy.sample

sealed class Destination(
    val route: String,
    val title: String,
    val description: String? = null,
) {
    data object Home : Destination(
        route = "home",
        title = "Lazy Sticky Headers",
    )

    data object ListVertical : Destination(
        route = "list/vertical",
        title = "LazyColumn",
        description = "Items are placed in a LazyColumn",
    )

    data object ListHorizontal : Destination(
        route = "list/horizontal",
        title = "LazyRow",
        description = "Items are placed in a LazyRow",
    )

    data object ListCalendar : Destination(
        route = "list/calendar",
        title = "Calendar",
        description = "Sample calendar schedule view",
    )
}

val topDestinations = listOf(
    Destination.ListVertical,
    Destination.ListHorizontal,
    Destination.ListCalendar,
)
