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

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsDialog(
    settings: DemoSettings,
    onUpdate: (DemoSettings) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
    ) {
        var themeSelectorOpen by remember { mutableStateOf(false) }
        var layoutDirSelectorOpen by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            ListItem(
                modifier = Modifier.clickable {
                    themeSelectorOpen = true
                },
                headlineContent = {
                    Text("Theme")
                },
                supportingContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.TopStart),
                    ) {
                        Text(settings.theme.text)

                        DropdownMenu(
                            expanded = themeSelectorOpen,
                            onDismissRequest = {
                                themeSelectorOpen = false
                            },
                        ) {
                            Theme.entries.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = it.text)
                                    },
                                    onClick = {
                                        onUpdate(settings.copy(theme = it))
                                        themeSelectorOpen = false
                                    },
                                )
                            }
                        }
                    }
                },
            )

            ListItem(
                modifier = Modifier.clickable {
                    layoutDirSelectorOpen = true
                },
                headlineContent = {
                    Text("Layout direction")
                },
                supportingContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.TopStart),
                    ) {
                        Text(
                            text = when (settings.layoutDirection) {
                                LayoutDirection.Ltr -> "LTR"
                                LayoutDirection.Rtl -> "RTL"
                                null -> "System"
                            },
                        )

                        DropdownMenu(
                            expanded = layoutDirSelectorOpen,
                            onDismissRequest = {
                                layoutDirSelectorOpen = false
                            },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = "System")
                                },
                                onClick = {
                                    onUpdate(settings.copy(layoutDirection = null))
                                    layoutDirSelectorOpen = false
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = "LTR")
                                },
                                onClick = {
                                    onUpdate(settings.copy(layoutDirection = LayoutDirection.Ltr))
                                    layoutDirSelectorOpen = false
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = "RTL")
                                },
                                onClick = {
                                    onUpdate(settings.copy(layoutDirection = LayoutDirection.Rtl))
                                    layoutDirSelectorOpen = false
                                },
                            )
                        }
                    }
                },
            )
        }
    }
}
