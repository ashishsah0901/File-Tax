package com.ashish.filetax.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF00695C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB2DFDB),
    onPrimaryContainer = Color(0xFF00251F),
    secondary = Color(0xFF37474F),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFBFDFC),
    onBackground = Color(0xFF17201E),
    surface = Color(0xFFFBFDFC),
    onSurface = Color(0xFF17201E),
    surfaceVariant = Color(0xFFEDF3F1),
    onSurfaceVariant = Color(0xFF45514E),
    outline = Color(0xFFB7C4C1)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7ECBBF),
    onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF005046),
    onPrimaryContainer = Color(0xFFB2DFDB),
    secondary = Color(0xFFB0BEC5),
    onSecondary = Color(0xFF1C2529),
    background = Color(0xFF101614),
    onBackground = Color(0xFFE1E5E3),
    surface = Color(0xFF101614),
    onSurface = Color(0xFFE1E5E3),
    surfaceVariant = Color(0xFF1D2523),
    onSurfaceVariant = Color(0xFFBFC9C6),
    outline = Color(0xFF56605D)
)

@Composable
fun FileTaxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}