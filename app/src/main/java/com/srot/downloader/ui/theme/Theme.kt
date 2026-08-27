package com.srot.downloader.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Teal = Color(0xFF0E6B66)
private val Foam = Color(0xFF7ED4CE)
private val Paper = Color(0xFFF3F0EA)
private val Ink = Color(0xFF141816)
private val SurfaceDark = Color(0xFF171C1B)
private val BgDark = Color(0xFF0E1211)

private val LightColors = lightColorScheme(
    primary = Teal,
    onPrimary = Color(0xFFF3FFFC),
    primaryContainer = Color(0xFFC8F0EB),
    onPrimaryContainer = Color(0xFF043734),
    background = Paper,
    onBackground = Ink,
    surface = Color(0xFFFFFCF7),
    onSurface = Ink,
    surfaceVariant = Color(0xFFE8E4DB),
    onSurfaceVariant = Color(0xFF5C615E),
)

private val DarkColors = darkColorScheme(
    primary = Foam,
    onPrimary = Color(0xFF003735),
    primaryContainer = Color(0xFF0F3F3C),
    onPrimaryContainer = Color(0xFFC8F0EB),
    background = BgDark,
    onBackground = Color(0xFFE7EBE8),
    surface = SurfaceDark,
    onSurface = Color(0xFFE7EBE8),
    surfaceVariant = Color(0xFF1F2624),
    onSurfaceVariant = Color(0xFF9AA39E),
)

@Composable
fun SrotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
