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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.gingerninja.lazy.StickyHeaders

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarRowListScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lazy Sticky Headers")
                        Text("Calendar row", style = MaterialTheme.typography.bodyMedium)
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
        CalendarRowList(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }
}

@Composable
private fun CalendarRowList(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    val startDate = remember {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        today.minus(today.dayOfMonth - 1, DateTimeUnit.DAY)
    }

    val formatter = LocalDate.Format {
        monthName(MonthNames.ENGLISH_FULL)
        chars(" ")
        year()
    }

    Column(
        modifier = modifier,
    ) {
        StickyHeaders(
            modifier = Modifier
                .fillMaxWidth(),
            state = listState,
            key = { item ->
                val date = startDate.plus(item.index, DateTimeUnit.DAY)

                LocalDate(date.year, date.month, 1)
            },
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                text = it.key.format(formatter),
                style = MaterialTheme.typography.labelSmall,
            )
        }

        LazyRow(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
        ) {
            horizontalListItems(startDate)
        }
    }
}

private fun LazyListScope.horizontalListItems(startDate: LocalDate) {
    items(
        count = Int.MAX_VALUE,
        key = { it },
    ) {
        val date = startDate.plus(it, DateTimeUnit.DAY)

        val formatter = LocalDate.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        }

        val dateHeader = formatter.format(date).let { day ->
            day.firstOrNull()?.toString() ?: day
        }

        Column(
            modifier = Modifier.width(50.dp),
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
