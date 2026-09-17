package com.example.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Immutable
data class HeroColors(
    val dayGradient: List<Color>,
    val nightGradient: List<Color>,
    val rainGradient: List<Color>,
    val onHero: Color,
    val onHeroMuted: Color,
)

val LocalHeroColors = staticCompositionLocalOf {
    HeroColors(
        dayGradient = emptyList(),
        nightGradient = emptyList(),
        rainGradient = emptyList(),
        onHero = Color.Unspecified,
        onHeroMuted = Color.Unspecified,
    )
}

val MaterialTheme.heroColors: HeroColors
    @Composable
    @ReadOnlyComposable
    get() = LocalHeroColors.current

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = Color(0xFFDDE6FF),
    onPrimaryContainer = Color(0xFF0B1F4D),
    secondary = BlueSecondary,
    onSecondary = White,
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
    onError = White,
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = Color(0xFF0A1A4D),
    primaryContainer = Color(0xFF2A3F73),
    onPrimaryContainer = Color(0xFFDCE5FF),
    secondary = BlueSecondaryDark,
    onSecondary = Color(0xFF003544),
    secondaryContainer = Color(0xFF145267),
    onSecondaryContainer = Color(0xFFC1F0FF),
    tertiary = BlueTertiaryDark,
    onTertiary = Color(0xFF1B2050),
    tertiaryContainer = Color(0xFF3B4175),
    onTertiaryContainer = Color(0xFFE0E3FF),
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    error = Error,
    onError = White,
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val heroDay = HeroColors(
        dayGradient = listOf(HeroDayTop, HeroDayBottom),
        nightGradient = listOf(HeroDayTop, HeroDayBottom),
        rainGradient = listOf(HeroDayRainTop, HeroDayRainBottom),
        onHero = HeroDayOnContent,
        onHeroMuted = HeroDayOnContentMuted,
    )
    val heroNight = HeroColors(
        dayGradient = listOf(HeroNightTop, HeroNightBottom),
        nightGradient = listOf(HeroNightTop, HeroNightBottom),
        rainGradient = listOf(HeroNightRainTop, HeroNightRainBottom),
        onHero = HeroNightOnContent,
        onHeroMuted = HeroNightOnContentMuted,
    )
    val heroColors = if (darkTheme) heroNight else heroDay
    CompositionLocalProvider(LocalHeroColors provides heroColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = WeatherTypography,
            content = content,
        )
    }
}