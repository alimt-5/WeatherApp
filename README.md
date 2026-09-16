# WeatherApp

A weather application for Android built with Kotlin and Jetpack Compose.

## Features

- Search for weather by city name.
- Get weather using the device's current location.
- Request location permission when current-location mode is used.
- Detect when device Location services are disabled and provide a shortcut to Location Settings.
- Display current temperature and weather condition.
- Display feels-like temperature, wind speed, humidity, and pressure.
- Display the location's local time.
- Show day/night state.
- Change the background based on the current weather condition and time of day.
- Save the last searched location with DataStore.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Clean Architecture
- Coroutines
- StateFlow
- Dagger Hilt
- Retrofit
- Gson
- OkHttp
- DataStore Preferences
- Google Play Services Location
- Coil

## Architecture

The project is organized into `core`, `data`, `domain`, `presentation`, and `ui` packages.

```text
com.example.weatherapp
├── core
│   ├── di
│   └── network
├── data
│   ├── di
│   ├── local
│   ├── remote
│   └── repository
├── domain
│   ├── location
│   ├── model
│   ├── repository
│   └── usecase
├── presentation
│   └── weather
└── ui
    └── theme
```

### Domain

Contains domain models, repository contracts, use cases, and the location-disabled exception.

### Data

Contains the WeatherAPI service, DTOs, mapper, remote data source, repositories, DataStore implementation, and Android location implementation.

### Presentation

Contains the weather screen, UI state, UI events, and `WeatherViewModel`.

### Core

Contains dependency-injection modules and the network base URL configuration.

### UI

Contains the Compose theme, colors, and typography.

## API

The app uses [WeatherAPI](https://www.weatherapi.com/) and its current-weather endpoint.

Base URL:

```text
https://api.weatherapi.com/v1/
```

The application requests the `current.json` endpoint with an API key and a location query.

## Setup

Create `local.properties` in the project root and add your WeatherAPI key:

```properties
WEATHER_API_KEY=YOUR_API_KEY
```

The key is read from `local.properties` and exposed to the app as `BuildConfig.WEATHER_API_KEY`.

Do not commit `local.properties` to Git.

## Requirements

- Android Studio
- JDK 17
- Android SDK 35
- Minimum Android version: API 28

## Project Configuration

- Kotlin: `2.1.21`
- Android Gradle Plugin: `8.6.0`
- Compile SDK: `35`
- Target SDK: `35`
- Min SDK: `28`
- Gradle: `8.7`

## Run

1. Clone the repository.
2. Open the project in Android Studio.
3. Add `WEATHER_API_KEY` to `local.properties`.
4. Sync the project with Gradle.
5. Build and run the `app` configuration.
