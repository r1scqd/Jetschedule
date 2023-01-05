package ru.rescqd.jetschedule.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AudienceColorScheme(
    val one: Color,
    val two: Color,
    val three: Color,
    val char: Color = one
)

val LocalAudienceColorScheme = staticCompositionLocalOf<AudienceColorScheme> {
    error("audience color scheme no define")
}