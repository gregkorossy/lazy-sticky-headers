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
package me.gingerninja.lazy.sample.grid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.gingerninja.lazy.sample.Destination
import me.gingerninja.lazy.sample.ui.component.NavScreen

fun NavGraphBuilder.gridScreens(
    onBack: () -> Unit,
    onScreenClick: (destination: Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    navigation(
        startDestination = "grid/root",
        route = GridDestinations.root.route,
    ) {
        composable("grid/root") {
            GridScreen(
                onBack = onBack,
                onScreenClick = onScreenClick,
                modifier = modifier,
            )
        }

        composable(GridDestinations.SimpleVerticalGrid.route) {
            SimpleVerticalGridScreen(
                modifier = modifier,
                onBack = onBack,
            )
        }
    }
}

object GridDestinations {
    val root = Destination(
        route = "grid",
        title = "Grids",
        description = "LazyVerticalGrid and LazyHorizontalGrid examples",
    )

    internal val SimpleVerticalGrid = Destination(
        route = "grid/simple-vertical",
        title = "Simple LazyVerticalGrid",
        description = "A vertical grid",
    )
}

private val destinations = listOf(
    GridDestinations.SimpleVerticalGrid,
)

@Composable
private fun GridScreen(
    onBack: () -> Unit,
    onScreenClick: (destination: Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavScreen(
        title = GridDestinations.root.title,
        subtitle = GridDestinations.root.description,
        destinations = destinations,
        onScreenClick = onScreenClick,
        modifier = modifier,
        onBack = onBack,
    )
}
