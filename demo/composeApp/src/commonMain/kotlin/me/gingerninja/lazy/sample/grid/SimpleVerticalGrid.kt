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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.gingerninja.lazy.StickyHeaders

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleVerticalGridScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lazy Sticky Headers")
                        Text("Simple LazyVerticalGrid", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) {
        SimpleVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }
}

@Composable
private fun SimpleVerticalGrid(modifier: Modifier = Modifier) {
    val gridState = rememberLazyGridState()

    Box(modifier = modifier) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(100.dp),
            state = gridState,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(start = 70.dp, end = 10.dp),
        ) {
            items(
                count = 200,
                span = {
                    if (it % 10 == 0) {
                        GridItemSpan(maxLineSpan)
                    } else {
                        GridItemSpan(1)
                    }
                },
            ) {
                Card {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .run {
                                if (it % 10 == 0) {
                                    this
                                } else {
                                    aspectRatio(1f)
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "$it", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        StickyHeaders(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxHeight()
                .width(50.dp)
                .align(Alignment.CenterStart),
            state = gridState,
            key = {
                it.firstOrNull()?.index?.div(10)
            },
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "${it.key}", textAlign = TextAlign.Center)
                }
            }
        }
    }
}
