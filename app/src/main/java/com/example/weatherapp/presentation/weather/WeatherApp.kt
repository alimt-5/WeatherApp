package com.example.weatherapp.presentation.weather


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun WeatherApp(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            viewModel.onLocationPermissionResult(granted = fineGranted || coarseGranted)
        }

    val openLocationSettings =
        remember { { context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) } }

    val openAppSettings = remember {
        {
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
            )
        }
    }

    WeatherAppTheme {
        WeatherScreen(
            state = state,
            onEvent = { event ->
                when (event) {
                    WeatherEvent.UseCurrentLocation -> {
                        permissionLauncher.launch(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    }
                    else -> {
                        viewModel.onEvent(event)
                    }
                }
            },
            onOpenLocationSettings = openLocationSettings,
            onOpenAppSettings = openAppSettings
        )
    }
}