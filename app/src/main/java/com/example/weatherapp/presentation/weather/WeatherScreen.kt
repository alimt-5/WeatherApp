package com.example.weatherapp.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.DeviceThermostat
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.ui.theme.heroColors

@Composable
fun WeatherScreen(
    state: WeatherUiState,
    onEvent: (WeatherEvent) -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenAppSettings: () -> Unit,
) {
    val hero = MaterialTheme.heroColors
    val weather = (state as? WeatherUiState.Success)?.weather
    val background = weatherBackground(weather?.current)

    var query by remember { mutableStateOf("") }
    var showBlankQueryHint by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (state) {
            is WeatherUiState.Success -> query = state.weather.location.name
            is WeatherUiState.Error -> if (state.query.isNotBlank()) query = state.query
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Weather",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = hero.onHero,
                    )
                    if (weather != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = hero.onHeroMuted,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${weather.location.name}, ${weather.location.country}",
                                color = hero.onHeroMuted,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
                IconButton(onClick = { onEvent(WeatherEvent.Refresh) }) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Refresh",
                        tint = hero.onHero,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SearchField(
                query = query,
                onQueryChange = {
                    query = it
                    showBlankQueryHint = false
                },
                onSearch = {
                    val trimmed = query.trim()
                    if (trimmed.isBlank()) {
                        showBlankQueryHint = true
                    } else {
                        onEvent(WeatherEvent.QueryChanged(trimmed))
                        onEvent(WeatherEvent.Search)
                    }
                },
                onLocation = { onEvent(WeatherEvent.UseCurrentLocation) },
                isError = showBlankQueryHint,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.weight(1f)) {
                when (state) {
                    WeatherUiState.Loading -> LoadingContent()

                    is WeatherUiState.Empty -> EmptyContent(message = state.message)

                    is WeatherUiState.Success -> WeatherDetails(weather = state.weather)

                    is WeatherUiState.Error -> ErrorContent(
                        state = state,
                        onEvent = onEvent,
                        onOpenLocationSettings = onOpenLocationSettings,
                        onOpenAppSettings = onOpenAppSettings,
                    )
                }
            }
        }
    }
}


@Composable
private fun WeatherDetails(weather: Weather) {
    val hero = MaterialTheme.heroColors

    Column(modifier = Modifier.fillMaxSize()) {
        MainWeatherCard(current = weather.current)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Details",
            color = hero.onHero,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(weatherMetricItems(weather.current)) { metric ->
                MetricCard(metric)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Local time · ${weather.location.localTime}",
            color = hero.onHeroMuted.copy(alpha = 0.75f),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}



private data class MetricItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
)

private fun weatherMetricItems(current: CurrentWeather) = listOf(
    MetricItem(
        "Feels like",
        "${format(current.feelsLikeCelsius)}°C",
        Icons.Rounded.DeviceThermostat
    ),
    MetricItem("Wind", "${format(current.windKph)} km/h", Icons.Rounded.Air),
    MetricItem("Humidity", "${current.humidityPercent}%", Icons.Rounded.Opacity),
    MetricItem("Pressure", "${format(current.pressureMb)} mb", Icons.Rounded.Speed),
)

private fun format(value: Double): String = if (value % 1.0 == 0.0) value.toInt().toString() else "%.1f".format(value)

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onLocation: () -> Unit,
    isError: Boolean = false,
) {
    val hero = MaterialTheme.heroColors

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = isError,
                label = { Text("Search city") },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Rounded.Search, contentDescription = "Search")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = hero.onHero,
                    unfocusedTextColor = hero.onHero,
                    focusedContainerColor = hero.onHero.copy(alpha = 0.12f),
                    unfocusedContainerColor = hero.onHero.copy(alpha = 0.08f),
                    cursorColor = hero.onHero,
                    focusedBorderColor = hero.onHero.copy(alpha = 0.75f),
                    unfocusedBorderColor = hero.onHero.copy(alpha = 0.35f),
                    focusedLabelColor = hero.onHero.copy(alpha = 0.9f),
                    unfocusedLabelColor = hero.onHeroMuted,
                    focusedLeadingIconColor = hero.onHero,
                    unfocusedLeadingIconColor = hero.onHeroMuted,
                    focusedTrailingIconColor = hero.onHero,
                    unfocusedTrailingIconColor = hero.onHeroMuted,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorTrailingIconColor = MaterialTheme.colorScheme.error,
                ),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onLocation,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(hero.onHero.copy(alpha = 0.14f)),
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "Use current location",
                    tint = hero.onHero,
                )
            }
        }

        if (isError) {
            Text(
                text = "Enter a city name.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            )
        }
    }
}

@Composable
private fun MainWeatherCard(current: CurrentWeather) {
    val hero = MaterialTheme.heroColors

    Surface(
        color = hero.onHero.copy(alpha = 0.12f),
        contentColor = hero.onHero,
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = current.iconUrl,
                contentDescription = current.condition,
                modifier = Modifier.size(112.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${format(current.temperatureCelsius)}°",
                    color = hero.onHero,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = current.condition,
                    color = hero.onHeroMuted,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (current.isDay) "Daytime" else "Nighttime",
                    color = hero.onHeroMuted.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun MetricCard(item: MetricItem) {
    val hero = MaterialTheme.heroColors

    Surface(
        color = hero.onHero.copy(alpha = 0.10f),
        contentColor = hero.onHero,
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = hero.onHeroMuted,
                modifier = Modifier.size(26.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = item.label,
                    color = hero.onHeroMuted,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = item.value,
                    color = hero.onHero,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}


@Composable
private fun LoadingContent() {
    val hero = MaterialTheme.heroColors
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = hero.onHero)
    }
}

@Composable
private fun EmptyContent(message: String) {
    val hero = MaterialTheme.heroColors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = hero.onHeroMuted,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ErrorContent(
    state: WeatherUiState.Error,
    onEvent: (WeatherEvent) -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenAppSettings: () -> Unit,
) {
    val hero = MaterialTheme.heroColors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = state.message,
                color = hero.onHero,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            when {
                state.isLocationDisabled -> Button(onClick = onOpenLocationSettings) {
                    Text("Open Location Settings")
                }

                state.isPermissionDenied -> Button(onClick = onOpenAppSettings) {
                    Text("Open App Settings")
                }

                state.query.isNotBlank() -> TextButton(onClick = { onEvent(WeatherEvent.Search) }) {
                    Text("Try again", color = hero.onHero)
                }
            }
            if (state.canDismiss) {
                Spacer(modifier = Modifier.height(4.dp))
                TextButton(onClick = { onEvent(WeatherEvent.DismissError) }) {
                    Text("Back", color = hero.onHeroMuted)
                }
            }
        }
    }
}

@Composable
private fun weatherBackground(current: CurrentWeather?): Brush {
    val hero = MaterialTheme.heroColors

    return when {
        current == null -> Brush.verticalGradient(hero.dayGradient)
        current.condition.contains("rain", true) -> Brush.verticalGradient(hero.rainGradient)
        !current.isDay -> Brush.verticalGradient(hero.nightGradient)
        else -> Brush.verticalGradient(hero.dayGradient)
    }
}
