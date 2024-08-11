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
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo.Companion.UnknownColumn
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo.Companion.UnknownRow
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.jvm.JvmInline

/**
 * Contains useful information about an individual item in lazy grids like
 * [LazyVerticalGrid][androidx.compose.foundation.lazy.grid.LazyVerticalGrid] or
 * [LazyHorizontalGrid][androidx.compose.foundation.lazy.grid.LazyHorizontalGrid].
 */
@JvmInline
value class LazyGridItem(private val value: LazyGridItemInfo) {
    /**
     * The index of the item in the grid.
     */
    val index: Int get() = value.index

    /**
     * The key of the item which was passed to the item() or items() function.
     */
    val key: Any get() = value.key

    /**
     * The content type of the item which was passed to the item() or items() function.
     */
    val contentType: Any? get() = value.contentType

    /**
     * The main axis offset of the item in pixels. It is relative to the start of the lazy grid container.
     */
    val offset: IntOffset get() = value.offset

    /**
     * The main axis size of the item in pixels. Note that if you emit multiple layouts in the composable
     * slot for the item then this size will be calculated as the max of their sizes.
     */
    val size: IntSize get() = value.size

    /**
     * The row occupied by the top start point of the item.
     * If this is unknown, for example while this item is animating to exit the viewport and is
     * still visible, the value will be [UnknownRow].
     */
    val row: Int get() = value.row

    /**
     * The column occupied by the top start point of the item.
     * If this is unknown, for example while this item is animating to exit the viewport and is
     * still visible, the value will be [UnknownColumn].
     */
    val column: Int get() = value.column
}

/**
 * Creates and tracks sticky items belonging to a
 * [LazyVerticalGrid][androidx.compose.foundation.lazy.grid.LazyVerticalGrid] or a
 * [LazyHorizontalGrid][androidx.compose.foundation.lazy.grid.LazyHorizontalGrid] with [state].
 *
 * The items are grouped by the value returned by [key]. This grouping only occurs in
 * a consecutive order, meaning that if the function returns the same value for two non-consecutive
 * items, two sticky headers will be created, thus this is generally discouraged.
 * When the [key] returns `null`, it acts as a boundary for the sticky items before /
 * after.
 *
 * @param state the [LazyGridState] of the grid
 * @param key key factory function for the sticky items
 * @param modifier [Modifier] applied to the container of the sticky items
 * @param content sticky item content
 */
@Composable
fun <T : Any> StickyHeaders(
    state: LazyGridState,
    key: (item: List<LazyGridItem>) -> T?,
    // contentType: (item: LazyListItem) -> Any? = { null },
    modifier: Modifier = Modifier,
    content: @Composable (stickyKey: StickyInterval<T>) -> Unit,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val keyFactory = rememberUpdatedState(key)

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

    fun IntOffset.dirOffset(): Int {
        return if (orientation == Orientation.Vertical) {
            y
        } else {
            x
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

                    var listIndex = 0
                    var lastLane = -2

                    buildList {
                        for ((index, item) in items.withIndex()) {
                            val lane =
                                if (orientation == Orientation.Vertical) item.row else item.column

                            if (lane == UnknownRow || lane == UnknownColumn) {
                                continue
                            }

                            if (lastLane == -2) {
                                lastLane = lane
                            }

                            if (lastLane == lane) {
                                continue
                            }

                            val sameLaneItems = items.subList(listIndex, index).map(::LazyGridItem)
                            listIndex = index
                            lastLane = lane

                            val currentKey = keyFactory.value(sameLaneItems)

                            if (!initKeySet) {
                                initKeySet = true
                                lastKey = currentKey
                            }

                            if (lastKey != currentKey) {
                                val endIndex = sameLaneItems.first().index
                                lastKey?.also {
                                    add(
                                        StickyInterval(it, lastIndex, endIndex),
                                    )
                                }

                                lastKey = currentKey
                                lastIndex = endIndex
                            }
                        }

                        val sameLaneItems = items.subList(listIndex, items.size).map(::LazyGridItem)
                        val sameLaneKey = keyFactory.value(sameLaneItems)

                        if (!initKeySet) { // all items belong to the same lane
                            lastKey = sameLaneKey
                        } else if (lastKey != sameLaneKey) {
                            lastKey?.also {
                                add(
                                    StickyInterval(it, lastIndex, items[listIndex].index),
                                )
                            }

                            lastKey = sameLaneKey
                            lastIndex = items[listIndex].index
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
        keys.forEach { interval ->
            key(interval.key) { // TODO ReusableContentHost { }, see LazyLayoutItemContentFactory
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
                            val spacing = state.layoutInfo.mainAxisItemSpacing

                            val next = state.layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == interval.endIndex }

                            val item = state.layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == interval.startIndex }

                            val nextOffset = next?.offset?.dirOffset() ?: Int.MAX_VALUE

                            val beforePadding = state.layoutInfo.beforeContentPadding

                            val switchDirection =
                                reverseLayout.xor(isRtl && orientation == Orientation.Horizontal)

                            val direction = if (switchDirection) -1f else 1f

                            if (item == null) { // don't show the item if it's not visible anymore
                                alpha = 0f
                            } else {
                                if (orientation == Orientation.Vertical) {
                                    val y = (nextOffset - spacing - size.height + beforePadding)
                                        .coerceAtMost(0f)
                                    val offset =
                                        (item.offset.dirOffset() + beforePadding).coerceAtLeast(0)

                                    translationY = (offset + y) * direction
                                } else {
                                    val x = (nextOffset - spacing - size.width + beforePadding)
                                        .coerceAtMost(0f)
                                    val offset =
                                        (item.offset.dirOffset() + beforePadding).coerceAtLeast(0)

                                    translationX = (offset + x) * direction
                                }
                            }
                        },
                ) {
                    content(interval)
                }
            }
        }
    }
}
