package me.gingerninja.lazy.sample

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver

@Immutable
data class DemoSettings(
    val theme: Theme = Theme.AUTO,
    val isVertical: Boolean = true,
    val isInfinite: Boolean = true,
) {
    companion object {
        /**
         * The default [Saver] implementation for [DemoSettings].
         */
        val Saver = run {
            val themeKey = "theme"
            val isVerticalKey = "isVertical"
            val isInfiniteKey = "isInfinite"

            mapSaver(
                save = {
                    mapOf(
                        themeKey to it.theme.text,
                        isVerticalKey to it.isVertical,
                        isInfiniteKey to it.isInfinite,
                    )
                },
                restore = {
                    DemoSettings(
                        theme = it[themeKey] as Theme,
                        isVertical = it[isVerticalKey] as Boolean,
                        isInfinite = it[isInfiniteKey] as Boolean,
                    )
                }
            )
        }
    }
}

enum class Theme(val text: String) {
    AUTO("System"), LIGHT("Light"), DARK("Dark")
}