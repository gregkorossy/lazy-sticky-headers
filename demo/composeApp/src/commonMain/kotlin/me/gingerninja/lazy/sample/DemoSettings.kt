package me.gingerninja.lazy.sample

import androidx.compose.runtime.Immutable

@Immutable
data class DemoSettings(
    val isVertical: Boolean = true,
    val isInfinite: Boolean = true,
)