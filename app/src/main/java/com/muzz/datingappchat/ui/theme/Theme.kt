package com.muzz.datingappchat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Chat roles map onto standard slots: primary = sent bubble, surfaceVariant = received bubble, surfaceContainer = menus,
// onSurfaceVariant = secondary text, outlineVariant = borders and dividers.
private val LightColors = lightColorScheme(
    primary = MuzzPink,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Ink,
    surface = Color.White,
    surfaceContainer = Color.White,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = Slate,
    outlineVariant = Cloud,
)

private val DarkColors = darkColorScheme(
    primary = MuzzPink,
    onPrimary = Color.White,
    background = Night,
    onBackground = Snow,
    surface = Night,
    surfaceContainer = Graphite,
    onSurface = Snow,
    surfaceVariant = Graphite,
    onSurfaceVariant = Steel,
    outlineVariant = Charcoal,
)

// Dynamic colour is deliberately off: the chat should always carry the brand palette.
@Composable
fun MuzzDatingAppChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
