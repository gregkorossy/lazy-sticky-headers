package me.gingerninja.lazy.stickyheaders.sample

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.gingerninja.lazy.sample.App
import me.gingerninja.lazy.sample.Theme.AUTO
import me.gingerninja.lazy.sample.Theme.DARK
import me.gingerninja.lazy.sample.Theme.LIGHT

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                onSettingsUpdate = {
                    val style = when (it.theme) {
                        AUTO -> {
                            SystemBarStyle.auto(
                                Color.TRANSPARENT,
                                Color.TRANSPARENT
                            )
                        }

                        LIGHT -> {
                            SystemBarStyle.light(
                                Color.TRANSPARENT,
                                Color.TRANSPARENT
                            )
                        }

                        DARK -> {
                            SystemBarStyle.dark(
                                Color.TRANSPARENT
                            )
                        }
                    }

                    enableEdgeToEdge(
                        statusBarStyle = style,
                        navigationBarStyle = style
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}