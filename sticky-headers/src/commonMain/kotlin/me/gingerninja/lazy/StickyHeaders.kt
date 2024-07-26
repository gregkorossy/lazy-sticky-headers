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
package me.gingerninja.lazy

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer

@Immutable
private data class StickyInterval<T : Any>(
    val key: T,
    val startIndex: Int,
    val endIndex: Int,
)

/* TODO
    - scroll on the box? overscroll effect?
 */

/**
 * Creates and tracks sticky items belonging to a
 * [LazyColumn][androidx.compose.foundation.lazy.LazyColumn] or a
 * [LazyRow][androidx.compose.foundation.lazy.LazyRow] with [state].
 *
 * The items are grouped by the value returned by [stickyKeyFactory]. This grouping only occurs in
 * a consecutive order, meaning that if the function returns the same value for two non-consecutive
 * items, two sticky headers will be created, thus this is generally discouraged.
 * When the [stickyKeyFactory] returns `null`, it acts as a boundary for the sticky items before /
 * after.
 *
 * @param state the [LazyListState] of the list
 * @param stickyKeyFactory key factory function for the sticky items
 * @param modifier [Modifier] applied to the container of the sticky items
 * @param content sticky item content
 */
@Composable
fun <T : Any> StickyHeaders(
    state: LazyListState,
    stickyKeyFactory: (item: LazyListItemInfo) -> T?,
    modifier: Modifier = Modifier,
    content: @Composable (stickyKey: T) -> Unit,
) {
    val keyFactory = rememberUpdatedState(stickyKeyFactory)

    val orientation by remember(state) {
        derivedStateOf {
            state.layoutInfo.orientation
        }
    }

    val reverseLayout by remember(state) {
        derivedStateOf {
            state.layoutInfo.reverseLayout
        }
    }

    val keys: List<StickyInterval<T>> by remember(state) {
        derivedStateOf {
            state.layoutInfo.visibleItemsInfo
                .takeIf { it.isNotEmpty() }
                ?.let { items ->
                    var initKeySet = false
                    var lastKey: T? = null
                    var lastIndex = items.first().index

                    buildList {
                        items.forEach { item ->
                            val key = keyFactory.value(item)

                            if (!initKeySet) {
                                initKeySet = true
                                lastKey = key
                            }

                            if (lastKey != key) {
                                lastKey?.also {
                                    add(
                                        StickyInterval(it, lastIndex, item.index),
                                    )
                                }

                                lastKey = key
                                lastIndex = item.index
                            }
                        }

                        lastKey?.also {
                            add(
                                StickyInterval(it, lastIndex, items.last().index + 1),
                            )
                        }
                    }
                } ?: emptyList()
        }
    }

    Box(
        modifier = modifier.clipToBounds(),
    ) {
        keys.forEach { (key, start, end) ->
            key(key) { // TODO ReusableContentHost { }, see LazyLayoutItemContentFactory
                Box(
                    modifier = Modifier
                        .run {
                            if (orientation == Orientation.Horizontal && reverseLayout) {
                                align(Alignment.CenterEnd)
                            } else if (orientation == Orientation.Vertical && reverseLayout) {
                                align(Alignment.BottomCenter)
                            } else {
                                this
                            }
                        }
                        .graphicsLayer {
                            val next =
                                state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == end }
                            val item =
                                state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == start }

                            val nextOffset = next?.offset ?: Int.MAX_VALUE

                            val beforePadding = state.layoutInfo.beforeContentPadding

                            val direction = if (reverseLayout) -1f else 1f

                            if (item == null) { // don't show the item if it's not visible anymore
                                alpha = 0f
                            } else {
                                if (orientation == Orientation.Vertical) {
                                    val y = (nextOffset - size.height + beforePadding)
                                        .coerceAtMost(0f)
                                    val offset = (item.offset + beforePadding).coerceAtLeast(0)

                                    translationY = (offset + y) * direction
                                } else {
                                    val x = (nextOffset - size.width + beforePadding)
                                        .coerceAtMost(0f)
                                    val offset = (item.offset + beforePadding).coerceAtLeast(0)

                                    translationX = (offset + x) * direction
                                }
                            }
                        },
                ) {
                    content(key)
                }
            }
        }
    }
}
