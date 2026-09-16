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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Weather

@Composable
fun WeatherScreen(
    state: WeatherUiState,
    onEvent: (WeatherEvent) -> Unit,
    onOpenLocationSettings: () -> Unit
) {
    val weather = (state as? WeatherUiState.Success)?.weather
    val background = weatherBackground(weather?.current)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        when (state) {
            WeatherUiState.Loading -> {
                LoadingContent()
            }

            is WeatherUiState.Success -> {
                WeatherContent(
                    weather = state.weather,
                    onEvent = onEvent
                )
            }

            is WeatherUiState.Error -> {
                ErrorContent(
                    state = state,
                    onEvent = onEvent,
                    onOpenLocationSettings = onOpenLocationSettings
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weather: Weather,
    onEvent: (WeatherEvent) -> Unit
) {
    var query by remember(weather.location.name) {
        mutableStateOf(weather.location.name)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Weather",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.82f),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "${weather.location.name}, ${weather.location.country}",
                        color = Color.White.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            IconButton(
                onClick = {
                    onEvent(WeatherEvent.Refresh)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.White
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        SearchField(
            query = query,
            onQueryChange = {
                query = it
            },
            onSearch = {
                onEvent(
                    WeatherEvent.QueryChanged(query)
                )
                onEvent(
                    WeatherEvent.Search
                )
            },
            onLocation = {
                onEvent(
                    WeatherEvent.UseCurrentLocation
                )
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        MainWeatherCard(
            current = weather.current
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Details",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(
                weatherMetricItems(weather.current)
            ) { metric ->
                MetricCard(metric)
            }
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Local time · ${weather.location.localTime}",
            color = Color.White.copy(alpha = 0.68f),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private data class MetricItem(
    val label: String,
    val value: String,
    val icon: ImageVector
)

private fun weatherMetricItems(
    current: CurrentWeather
) = listOf(
    MetricItem(
        label = "Feels like",
        value = "${format(current.feelsLikeCelsius)}°C",
        icon = Icons.Rounded.DeviceThermostat
    ),
    MetricItem(
        label = "Wind",
        value = "${format(current.windKph)} km/h",
        icon = Icons.Rounded.Air
    ),
    MetricItem(
        label = "Humidity",
        value = "${current.humidityPercent}%",
        icon = Icons.Rounded.Opacity
    ),
    MetricItem(
        label = "Pressure",
        value = "${format(current.pressureMb)} mb",
        icon = Icons.Rounded.Speed
    )
)

private fun format(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        "%.1f".format(value)
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onLocation: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            label = {
                Text("Search city")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onSearch
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            ),
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        IconButton(
            onClick = onLocation,
            modifier = Modifier
                .size(56.dp)
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .background(
                    MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.92f
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = "Use current location"
            )
        }
    }
}

@Composable
private fun MainWeatherCard(
    current: CurrentWeather
) {
    Surface(
        color = Color.White.copy(alpha = 0.12f),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = current.iconUrl,
                contentDescription = current.condition,
                modifier = Modifier.size(112.dp)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${format(current.temperatureCelsius)}°",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = current.condition,
                    color = Color.White.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = if (current.isDay) {
                        "Daytime"
                    } else {
                        "Nighttime"
                    },
                    color = Color.White.copy(alpha = 0.62f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    item: MetricItem
) {
    Surface(
        color = Color.White.copy(alpha = 0.10f),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.82f),
                modifier = Modifier.size(26.dp)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {
                Text(
                    text = item.label,
                    color = Color.White.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = item.value,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White
        )
    }
}

@Composable
private fun ErrorContent(
    state: WeatherUiState.Error,
    onEvent: (WeatherEvent) -> Unit,
    onOpenLocationSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.message,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (state.isLocationDisabled) {
                Button(
                    onClick = onOpenLocationSettings
                ) {
                    Text("Open Location Settings")
                }
            } else if (state.query.isNotBlank()) {
                TextButton(
                    onClick = {
                        onEvent(WeatherEvent.Search)
                    }
                ) {
                    Text(
                        text = "Try again",
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun weatherBackground(
    current: CurrentWeather?
): Brush {
    return when {
        current == null -> {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF1B2330),
                    Color(0xFF34495E)
                )
            )
        }

        current.isDay && current.condition.contains(
            "rain",
            ignoreCase = true
        ) -> {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF356C8E),
                    Color(0xFF1C3141)
                )
            )
        }

        !current.isDay -> {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF17213A),
                    Color(0xFF27304F)
                )
            )
        }

        else -> {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF4CA1AF),
                    Color(0xFF2C3E50)
                )
            )
        }
    }
}