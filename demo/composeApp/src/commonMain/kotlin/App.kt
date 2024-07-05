import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.gingerninja.lazy.sample.DemoSettings
import me.gingerninja.lazy.sample.FINITE_ITEM_COUNT
import me.gingerninja.lazy.sample.SampleList
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var settings by remember {
        mutableStateOf(
            DemoSettings()
        )
    }
    var showSettings by rememberSaveable {
        mutableStateOf(false)
    }

    var isGrid by rememberSaveable {
        mutableStateOf(false)
    }

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Lazy Sticky Headers")
                },
                actions = {
                    IconButton(
                        onClick = {
                            showSettings = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                SegmentedButton(
                    selected = !isGrid,
                    onClick = { isGrid = false },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                ) {
                    Text(text = "List")
                }
                SegmentedButton(
                    selected = isGrid,
                    onClick = { isGrid = true },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                ) {
                    Text(text = "Grid")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!isGrid) {
                SampleList(
                    settings = settings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                // TODO
            }
        }
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
        Column {
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