package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AcademyBlueLight,
    onPrimary = Color.White,
    primaryContainer = AcademyBlueDark,
    onPrimaryContainer = Color.White,
    secondary = AcademyYellow,
    onSecondary = Color.Black,
    secondaryContainer = AcademyYellowDark,
    onSecondaryContainer = Color.White,
    tertiary = AcademyRedLight,
    onTertiary = Color.White,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = AcademyBlue,
    onPrimary = Color.White,
    primaryContainer = AcademyBlueContainer,
    onPrimaryContainer = AcademyBlueDark,
    secondary = AcademyYellow,
    onSecondary = Color(0xFF2E2400),
    secondaryContainer = AcademyYellowContainer,
    onSecondaryContainer = Color(0xFF4A3B00),
    tertiary = AcademyRed,
    onTertiary = Color.White,
    tertiaryContainer = AcademyRedContainer,
    onTertiaryContainer = Color(0xFF7A0000),
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    outline = OutlineLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand identity prominent
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
