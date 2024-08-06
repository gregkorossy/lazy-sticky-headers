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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.gingerninja.lazy.StickyHeaders
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarListScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lazy Sticky Headers")
                        Text(
                            ListDestinations.Calendar.title,
                            style = MaterialTheme.typography.bodyMedium,
                        )
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
        CalendarList(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }
}

@Composable
private fun CalendarList(modifier: Modifier) {
    val startIndex = remember {
        Int.MAX_VALUE / 2
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = startIndex,
    )

    val horizontalListState = rememberLazyListState(
        initialFirstVisibleItemIndex = startIndex,
    )

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    val startDate = remember(today) {
        today
    }

    LaunchedEffect(listState, horizontalListState) {
        launch {
            snapshotFlow { listState.firstVisibleItemIndex }
                .collectLatest {
                    horizontalListState.animateScrollToItem(
                        scheduleIndexToMonthIndex(it, startIndex, startDate),
                    )
                }
        }
    }

    Column(
        modifier = modifier,
    ) {
        MonthView(
            modifier = Modifier.padding(bottom = 12.dp),
            listState = horizontalListState,
            startIndex = startIndex,
            startDate = startDate,
        )

        ScheduleView(
            modifier = Modifier.weight(1f),
            listState = listState,
            startIndex = startIndex,
            startDate = startDate,
            today = today,
        )
    }
}

@Composable
private fun MonthView(
    listState: LazyListState,
    startIndex: Int,
    startDate: LocalDate,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        StickyHeaders(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            key = { item ->
                val date = startDate.plus(item.index - startIndex, DateTimeUnit.DAY)

                LocalDate(date.year, date.month, 1)
            },
        ) {
            val formatter = LocalDate.Format {
                monthName(MonthNames.ENGLISH_FULL)
                chars(" ")
                year()
            }

            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                text = it.key.format(formatter),
                style = MaterialTheme.typography.labelSmall,
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
        ) {
            monthViewDayItems(startIndex, startDate)
        }
    }
}

private fun LazyListScope.monthViewDayItems(startIndex: Int, startDate: LocalDate) {
    items(
        count = Int.MAX_VALUE,
        key = { it },
    ) {
        val date = startDate.plus(it - startIndex, DateTimeUnit.DAY)

        val formatter = LocalDate.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        }

        val dateHeader = formatter.format(date).let { day ->
            day.firstOrNull()?.toString() ?: day
        }

        Column(
            // modifier = Modifier.fillMaxWidth(),
            modifier = Modifier
                .width(50.dp),
            // .padding(horizontal = 10.dp, vertical = 10.dp)
            // .fillParentMaxWidth(1 / 7f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = dateHeader,
                style = MaterialTheme.typography.labelSmall,
            )

            Text(
                modifier = Modifier,
                text = "${date.dayOfMonth}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ScheduleView(
    listState: LazyListState,
    startIndex: Int,
    startDate: LocalDate,
    today: LocalDate,
    modifier: Modifier = Modifier,
) {
    fun getDataByIndex(index: Int) = calculateDataByIndex(index, startIndex, startDate)

    fun getItemTypeByIndex(index: Int): ScheduleItemType {
        return getDataByIndex(index).type
    }

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            items(
                count = Int.MAX_VALUE,
                key = { it },
                contentType = ::getItemTypeByIndex,
            ) {
                val data = getDataByIndex(it)

                when (data.type) {
                    ScheduleItemType.WEEK -> {
                        ScheduleWeek(
                            startDate = data.date,
                            today = today,
                            modifier = Modifier.padding(
                                start = 70.dp,
                                end = 20.dp,
                                top = 10.dp,
                                bottom = 10.dp,
                            ),
                        )
                    }

                    ScheduleItemType.ITEM -> {
                        ScheduleItemList(
                            date = data.date,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                }
            }
        }

        StickyHeaders(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxHeight(),
            state = listState,
            key = { item ->
                val itemKey = getDataByIndex(item.index)
                val itemType = item.contentType as ScheduleItemType

                if (itemType != ScheduleItemType.ITEM) {
                    null
                } else {
                    itemKey.date
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .width(50.dp)
                    // .padding(vertical = 10.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 10.dp, vertical = 0.dp)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val formatter = LocalDate.Format {
                    dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
                }
                Text(
                    text = it.key.format(formatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (today == it.key) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        Color.Unspecified
                    },
                )
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .run {
                            if (today == it.key) {
                                background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = CircleShape,
                                )
                            } else {
                                this
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        textAlign = TextAlign.Center,
                        text = "${it.key.dayOfMonth}",
                        color = if (today == it.key) {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleWeek(startDate: LocalDate, today: LocalDate, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = formatWeekDate(startDate, today),
        style = MaterialTheme.typography.labelMedium,
    )
}

@Composable
private fun ScheduleItemList(date: LocalDate, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        repeat(date.dayOfMonth % 3 + 1) {
            ScheduleItem(
                title = "The date is $date",
                subtitle = "Item #$it",
                modifier = Modifier
                    .padding(
                        start = 70.dp,
                        end = 20.dp,
                        top = 2.dp,
                        bottom = 8.dp,
                    )
                    .fillMaxWidth(),
            )
        }

        Spacer(
            Modifier.height(20.dp),
        )
    }
}

@Composable
private fun ScheduleItem(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        onClick = {},
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(subtitle)
        }
    }
}

private fun scheduleIndexToMonthIndex(index: Int, startIndex: Int, startDate: LocalDate): Int {
    val original = calculateDataByIndex(index, startIndex, startDate)

    return startIndex + startDate.daysUntil(original.date)
}

private fun calculateDataByIndex(
    index: Int,
    startIndex: Int,
    startDate: LocalDate,
): CalculatedDate {
    val weekStartDiff = startDate.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
    val diff = index - startIndex

    return if (index < startIndex) {
        val weekItemCount = ((diff - (7 - weekStartDiff)) / 8).absoluteValue
        val isWeek = -(diff + weekStartDiff + 1) % 8 == 0

        val date = startDate.plus(diff + weekItemCount, DateTimeUnit.DAY)

        CalculatedDate(
            date = date,
            type = if (isWeek) ScheduleItemType.WEEK else ScheduleItemType.ITEM,
        )
    } else {
        val weekItemCount = (diff + weekStartDiff + 1) / 8
        val isWeek = (diff + weekStartDiff + 1) % 8 == 0

        val date = startDate.plus(diff - weekItemCount + (if (isWeek) 1 else 0), DateTimeUnit.DAY)

        CalculatedDate(
            date = date,
            type = if (isWeek) ScheduleItemType.WEEK else ScheduleItemType.ITEM,
        )
    }
}

private fun formatWeekDate(date: LocalDate, today: LocalDate): String {
    val endOfWeek = date.plus(6, DateTimeUnit.DAY)

    val firstFormat = when {
        date.year != endOfWeek.year -> DateFormatters.dayMonthYear
        else -> DateFormatters.dayMonth
    }

    val secondFormat = when {
        date.year != endOfWeek.year -> DateFormatters.dayMonthYear
        date.month != endOfWeek.month && endOfWeek.year != today.year -> DateFormatters.dayMonthYear
        endOfWeek.year != today.year -> DateFormatters.dayYear
        date.month != endOfWeek.month -> DateFormatters.dayMonth
        else -> DateFormatters.day
    }

    return buildString {
        append(firstFormat.format(date))
        append(" – ")
        append(secondFormat.format(endOfWeek))
    }
}

private object DateFormatters {
    val day = LocalDate.Format {
        dayOfMonth(Padding.NONE)
    }

    val dayMonth = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth(Padding.NONE)
    }

    val dayMonthYear = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth(Padding.NONE)
        chars(", ")
        year()
    }

    val dayYear = LocalDate.Format {
        dayOfMonth(Padding.NONE)
        chars(", ")
        year()
    }
}

private class CalculatedDate(
    val date: LocalDate,
    val type: ScheduleItemType,
)

private enum class ScheduleItemType {
    WEEK,
    ITEM,
}
