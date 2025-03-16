package com.example.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
    const val API_KEY = "14a8b6a7f8eb41019ad172754250702"
    const val BASE_URL = "https://api.weatherapi.com/v1/"

    // Define a function called isNetworkAvailable that takes a Context parameter and returns a Boolean value
    fun isNetworkAvailable(context: Context): Boolean {
        //Get an instance of ConnectivityManager using the context parameter
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Check if the device's API is level 23 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get the currently active network and return false if there is none
            val network = connectivityManager.activeNetwork ?: return false

            //Get the capabilities of the active network and return false if it has none
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            //Check the type of the active network and return true if it is either WIFI, CELLULAR, or ETHERNET

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                else -> return false
            }

        } else {
            // For API levels below 23, get the active network info an return true if it is connected or connecting
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}