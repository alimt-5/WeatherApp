package com.example.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = Color(0xFFDDE6FF),
    onPrimaryContainer = Color(0xFF10204D),
    secondary = BlueSecondary,
    onSecondary = Color(0xFF003548),
    secondaryContainer = Color(0xFFC8F0FF),
    onSecondaryContainer = Color(0xFF001F2A),
    tertiary = BlueTertiary,
    onTertiary = White,
    tertiaryContainer = Color(0xFFE1E4FF),
    onTertiaryContainer = Color(0xFF11194D),
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    error = Error,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = Color(0xFF12234D),
    primaryContainer = Color(0xFF314B8D),
    onPrimaryContainer = Color(0xFFDCE5FF),
    secondary = BlueSecondaryDark,
    onSecondary = Color(0xFF003544),
    secondaryContainer = Color(0xFF145267),
    onSecondaryContainer = Color(0xFFC1F0FF),
    tertiary = BlueTertiaryDark,
    onTertiary = Color(0xFF252A5B),
    tertiaryContainer = Color(0xFF3B4175),
    onTertiaryContainer = Color(0xFFE0E3FF),
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    error = Error,
    onError = White
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WeatherTypography,
        content = content
    )
}