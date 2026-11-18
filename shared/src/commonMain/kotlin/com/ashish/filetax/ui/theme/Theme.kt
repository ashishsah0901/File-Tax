package com.ashish.filetax.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF00695C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB2DFDB),
    onPrimaryContainer = Color(0xFF00251F),
    secondary = Color(0xFF37474F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7E4E1),
    onSecondaryContainer = Color(0xFF1B2A27),
    background = Color(0xFFFBFDFC),
    onBackground = Color(0xFF17201E),
    surface = Color(0xFFFBFDFC),
    onSurface = Color(0xFF17201E),
    surfaceVariant = Color(0xFFEDF3F1),
    onSurfaceVariant = Color(0xFF45514E),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF4F8F7),
    surfaceContainer = Color(0xFFEDF3F1),
    surfaceContainerHigh = Color(0xFFE4ECEA),
    surfaceContainerHighest = Color(0xFFDCE5E2),
    outline = Color(0xFFB7C4C1),
    outlineVariant = Color(0xFFD5DFDC)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7ECBBF),
    onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF005046),
    onPrimaryContainer = Color(0xFFB2DFDB),
    secondary = Color(0xFFB0BEC5),
    onSecondary = Color(0xFF1C2529),
    secondaryContainer = Color(0xFF2B3A37),
    onSecondaryContainer = Color(0xFFD7E4E1),
    background = Color(0xFF101614),
    onBackground = Color(0xFFE1E5E3),
    surface = Color(0xFF101614),
    onSurface = Color(0xFFE1E5E3),
    surfaceVariant = Color(0xFF1D2523),
    onSurfaceVariant = Color(0xFFBFC9C6),
    surfaceContainerLowest = Color(0xFF0B100F),
    surfaceContainerLow = Color(0xFF171E1C),
    surfaceContainer = Color(0xFF1D2523),
    surfaceContainerHigh = Color(0xFF252E2B),
    surfaceContainerHighest = Color(0xFF2F3936),
    outline = Color(0xFF56605D),
    outlineVariant = Color(0xFF33403C)
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

private val AppTypography = Typography().let { base ->
    Typography(
        displaySmall = base.displaySmall.copy(fontSize = 40.sp, lineHeight = 48.sp),
        headlineSmall = base.headlineSmall.copy(fontSize = 28.sp, lineHeight = 36.sp),
        titleMedium = base.titleMedium.copy(fontSize = 18.sp, lineHeight = 26.sp),
        titleSmall = base.titleSmall.copy(fontSize = 16.sp, lineHeight = 24.sp),
        bodyLarge = base.bodyLarge.copy(fontSize = 17.sp, lineHeight = 26.sp),
        bodyMedium = base.bodyMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
        bodySmall = base.bodySmall.copy(fontSize = 14.sp, lineHeight = 20.sp),
        labelLarge = base.labelLarge.copy(fontSize = 16.sp, lineHeight = 22.sp),
        labelMedium = base.labelMedium.copy(fontSize = 13.sp, lineHeight = 18.sp),
        labelSmall = base.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp)
    )
}

@Composable
fun FileTaxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        shapes = AppShapes,
        typography = AppTypography,
        content = content
    )
}