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

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.gingerninja.lazy.sample.grid.gridScreens
import me.gingerninja.lazy.sample.home.homeScreen
import me.gingerninja.lazy.sample.list.listScreens
import me.gingerninja.lazy.sample.ui.theme.LazySampleTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(onSettingsUpdate: (DemoSettings) -> Unit = {}) {
    var settings by rememberSaveable(stateSaver = DemoSettings.Saver) {
        mutableStateOf(
            DemoSettings(),
        )
    }

    LaunchedEffect(settings) {
        onSettingsUpdate(settings)
    }

    var showSettings by rememberSaveable {
        mutableStateOf(false)
    }

    val darkTheme = when (settings.theme) {
        Theme.AUTO -> isSystemInDarkTheme()
        Theme.LIGHT -> false
        Theme.DARK -> true
    }

    val layoutDirection = settings.layoutDirection ?: LocalLayoutDirection.current

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        LazySampleTheme(
            darkTheme = darkTheme,
        ) {
            if (showSettings) {
                SettingsDialog(
                    settings = settings,
                    onUpdate = {
                        settings = it
                    },
                    onDismiss = {
                        showSettings = false
                    },
                )
            }

            AppContent(
                showSettings = {
                    showSettings = true
                },
            )
        }
    }
}

@Composable
private fun AppContent(showSettings: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        AppNavHost(
            modifier = Modifier.fillMaxSize(),
            showSettings = showSettings,
        )
    }
}

@Composable
private fun AppNavHost(showSettings: () -> Unit, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home.route,
        enterTransition = {
            fadeIn() + slideInHorizontally { it / 2 }
        },
        exitTransition = {
            fadeOut() + slideOutHorizontally { -it / 2 }
        },
        popEnterTransition = {
            fadeIn() + slideInHorizontally { -it / 2 }
        },
        popExitTransition = {
            fadeOut() + slideOutHorizontally { it / 2 }
        },
    ) {
        homeScreen(
            onSettingsClick = showSettings,
            onScreenClick = { navController.navigate(it.route) },
            modifier = Modifier.fillMaxSize(),
        )

        listScreens(
            modifier = Modifier.fillMaxSize(),
            onScreenClick = { navController.navigate(it.route) },
            onBack = {
                navController.popBackStack()
            },
        )

        gridScreens(
            modifier = Modifier.fillMaxSize(),
            onScreenClick = { navController.navigate(it.route) },
            onBack = {
                navController.popBackStack()
            },
        )
    }
}
