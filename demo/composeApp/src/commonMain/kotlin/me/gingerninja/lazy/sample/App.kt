package me.gingerninja.lazy.sample

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.gingerninja.lazy.sample.list.FINITE_ITEM_COUNT
import me.gingerninja.lazy.sample.list.calendarList
import me.gingerninja.lazy.sample.list.sampleList
import me.gingerninja.lazy.sample.ui.theme.LazySampleTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    onSettingsUpdate: (DemoSettings) -> Unit = {}
) {
    var settings by rememberSaveable(stateSaver = DemoSettings.Saver) {
        mutableStateOf(
            DemoSettings()
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

    LazySampleTheme(
        darkTheme = darkTheme
    ) {
        if (showSettings) {
            SettingsDialog(
                settings = settings,
                onUpdate = {
                    settings = it
                },
                onDismiss = {
                    showSettings = false
                }
            )
        }

        AppContent(
            settings = settings,
            showSettings = {
                showSettings = true
            }
        )
    }
}

@Composable
private fun AppContent(
    settings: DemoSettings,
    showSettings: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavHost(
            modifier = Modifier.fillMaxSize(),
            settings = settings,
            showSettings = showSettings,
        )
    }
}

@Composable
private fun AppNavHost(
    settings: DemoSettings,
    showSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.Home.route,
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

        sampleList(
            modifier = Modifier.fillMaxSize(),
            onBack = {
                navController.popBackStack()
            },
        )

        calendarList(
            modifier = Modifier.fillMaxSize(),
            onBack = {
                navController.popBackStack()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDialog(
    settings: DemoSettings,
    onUpdate: (DemoSettings) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        var themeSelectorOpen by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            ListItem(
                modifier = Modifier.clickable {
                    themeSelectorOpen = true
                },
                headlineContent = {
                    Text("Theme")
                },
                supportingContent = {
                    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
                        Text(settings.theme.text)

                        DropdownMenu(
                            expanded = themeSelectorOpen,
                            onDismissRequest = {
                                themeSelectorOpen = false
                            }
                        ) {
                            Theme.entries.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = it.text)
                                    },
                                    onClick = {
                                        onUpdate(settings.copy(theme = it))
                                        themeSelectorOpen = false
                                    }
                                )
                            }
                        }
                    }
                },
            )
            ListItem(
                modifier = Modifier.clickable {
                    onUpdate(settings.copy(isVertical = !settings.isVertical))
                },
                headlineContent = {
                    Text("Vertical layout")
                },
                supportingContent = {
                    val countText = if (settings.isVertical) {
                        "column"
                    } else {
                        "row"
                    }

                    Text("Showing items in a $countText")
                },
                trailingContent = {
                    Switch(
                        checked = settings.isVertical,
                        onCheckedChange = {
                            onUpdate(settings.copy(isVertical = !settings.isVertical))
                        }
                    )
                }
            )

            ListItem(
                modifier = Modifier.clickable {
                    onUpdate(settings.copy(isInfinite = !settings.isInfinite))
                },
                headlineContent = {
                    Text("Infinite items")
                },
                supportingContent = {
                    val countText = if (settings.isInfinite) {
                        "infinite"
                    } else {
                        FINITE_ITEM_COUNT.toString()
                    }

                    Text("Displaying $countText items")
                },
                trailingContent = {
                    Switch(
                        checked = settings.isInfinite,
                        onCheckedChange = {
                            onUpdate(settings.copy(isInfinite = !settings.isInfinite))
                        }
                    )
                }
            )
        }
    }
}