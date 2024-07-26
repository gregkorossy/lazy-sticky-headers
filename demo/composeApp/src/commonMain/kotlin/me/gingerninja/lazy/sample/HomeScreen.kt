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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.gingerninja.lazy.sample.ui.component.NavCard

fun NavGraphBuilder.homeScreen(
    onSettingsClick: () -> Unit,
    onScreenClick: (destination: Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable(Destination.Home.route) {
        HomeScreen(
            onSettingsClick = onSettingsClick,
            onScreenClick = onScreenClick,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onSettingsClick: () -> Unit,
    onScreenClick: (destination: Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(Destination.Home.title)
                },
                actions = {
                    IconButton(
                        onClick = onSettingsClick,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                        )
                    }
                },
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            items(topDestinations) { destination ->
                NavCard(
                    title = destination.title,
                    description = destination.description,
                    onClick = {
                        onScreenClick(destination)
                    },
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                )
            }
        }
    }
}
