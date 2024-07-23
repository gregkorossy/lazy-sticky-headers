package me.gingerninja.lazy.sample.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
import me.gingerninja.lazy.sample.DemoSettings
import me.gingerninja.lazy.sample.Destination

internal fun NavGraphBuilder.sampleList(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable(Destination.ListVertical.route) {
        SampleListScreen(
            onBack = onBack,
            settings = DemoSettings(isVertical = true),
            title = Destination.ListVertical.title,
            modifier = modifier,
        )
    }

    composable(Destination.ListHorizontal.route) {
        SampleListScreen(
            onBack = onBack,
            settings = DemoSettings(isVertical = false),
            title = Destination.ListHorizontal.title,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SampleListScreen(
    onBack: () -> Unit,
    settings: DemoSettings,
    title: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(title)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                /*actions = {
                    IconButton(
                        onClick = {
                            showSettings()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }*/
            )
        }
    ) {
        SampleList(
            settings = settings,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
private fun SampleList(
    settings: DemoSettings,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val startDate = remember {
        val today =
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        /*val diff = today.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber

        today.minus(diff, DateTimeUnit.DAY)*/
        today.minus(today.dayOfMonth - 1, DateTimeUnit.DAY)
    }

    if (settings.isVertical) {
        Box(modifier = modifier) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                verticalListItems(startDate, settings)
            }

            StickyHeaders(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxHeight(),
                state = listState,
                stickyKeyFactory = { item ->
                    val date = startDate.plus(item.index, DateTimeUnit.DAY)

                    if (date.dayOfMonth % 3 == 0) {
                        null
                    } else {
                        date
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .width(50.dp)
                        .padding(vertical = 10.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val formatter = LocalDate.Format {
                        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
                    }
                    Text(
                        text = it.format(formatter),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text("${it.dayOfMonth}")
                }
            }

            /*LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                state = listState
            ) {
                verticalListItems(startDate, settings)
            }*/
        }
    } else {
        Column(
            modifier = modifier
        ) {
            StickyHeaders(
                modifier = Modifier
                    .fillMaxWidth(),
                state = listState,
                stickyKeyFactory = { item ->
                    val date = startDate.plus(item.index, DateTimeUnit.DAY)

                    LocalDate(date.year, date.month, 1)
                }
            ) {
                val formatter = LocalDate.Format {
                    monthName(MonthNames.ENGLISH_FULL)
                    chars(" ")
                    year()
                }

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    text = it.format(formatter),
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState
            ) {
                horizontalListItems(startDate, settings)
            }
        }
    }
}

private fun LazyListScope.verticalListItems(
    startDate: LocalDate,
    settings: DemoSettings
) {
    items(
        count = if (settings.isInfinite) Int.MAX_VALUE else FINITE_ITEM_COUNT,
        key = { it }
    ) {
        val date = startDate.plus(it, DateTimeUnit.DAY)

        if (date.dayOfMonth % 3 != 0) {
            Card(
                onClick = {},
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                    .padding(start = 70.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Day card",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "$date",
                    )
                }
            }
        } else {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(40.dp)
            ) {
                Text("Full-size item at $date")
            }
        }
    }
}

private fun LazyListScope.horizontalListItems(
    startDate: LocalDate,
    settings: DemoSettings
) {
    items(
        count = if (settings.isInfinite) Int.MAX_VALUE else FINITE_ITEM_COUNT,
        key = { it }
    ) {
        val date = startDate.plus(it, DateTimeUnit.DAY)

        val formatter = LocalDate.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        }

        val dateHeader = formatter.format(date).let { day ->
            day.firstOrNull()?.toString() ?: day
        }

        Column(
            //modifier = Modifier.fillMaxWidth(),
            modifier = Modifier
                .width(50.dp),
            //.padding(horizontal = 10.dp, vertical = 10.dp)
            //.fillParentMaxWidth(1 / 7f),
            horizontalAlignment = Alignment.CenterHorizontally
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

const val FINITE_ITEM_COUNT = 100