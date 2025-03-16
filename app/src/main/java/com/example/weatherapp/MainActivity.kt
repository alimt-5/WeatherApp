package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.weatherapp.models.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private val REQUEST_LOCATION_CODE = 123123
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var timer: Timer
    private lateinit var edtArea: EditText
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtArea = findViewById(R.id.edt)
        edtArea.isSingleLine = true
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        saveText()
        appIcon()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    getLocationWeatherDetails()
                    Log.e("time", "yes")
                }
            }
        }, 0, 2000)

    }

    private fun appIcon(){
        val taskDescription = ActivityManager.TaskDescription(
            getString(R.string.app_name),
            BitmapFactory.decodeResource(resources, R.drawable.cloudy)
        )
        setTaskDescription(taskDescription)
    }

    private fun saveText() {
        edtArea.setText(sharedPreferences.getString("savedText", ""))
        edtArea.addTextChangedListener(object :TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sharedPreferences.edit().putString("savedText", s.toString()).apply()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE && grantResults.size > 0) {
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            requestLocationData()
        } else {
            Toast.makeText(this, "The permission was not granted", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData() {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).build()
        mFusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                getLocationWeatherDetails()
            }
        }, Looper.myLooper())
    }

    private fun getLocationWeatherDetails() {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            val serviceApi = retrofit.create(WeatherServiceApi::class.java)

            val call = serviceApi.getCurrentWeather(Constants.API_KEY, edtArea.text.toString())

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weather = response.body()
                        Log.d("WeatherResponse", response.toString())
                        findViewById<TextView>(R.id.text_view_location_name_region_country).text =
                            weather?.location?.name.toString() + "/" + weather?.location?.region.toString() + "/" + weather?.location?.country.toString()
                        findViewById<TextView>(R.id.localtime).text =
                            weather?.location?.localtime.toString()
                        findViewById<TextView>(R.id.text_view_condition_text).text =
                            weather?.current?.condition?.text.toString()
                        findViewById<TextView>(R.id.text_view_current_temp_C).text =
                            weather?.current?.temp_c.toString() + "°C"
                        val imageView: ImageView = findViewById(R.id.Icon)
                        val imageUrl = "https:" + weather?.current?.condition?.icon.toString()
                        Glide.with(this@MainActivity).load(imageUrl).into(imageView)
                        findViewById<TextView>(R.id.text_view_feelslike_c).text =
                            weather?.current?.feelslike_c.toString() + "°C"
                        findViewById<TextView>(R.id.text_view_wind_kph).text =
                            weather?.current?.wind_kph.toString() + "km/h"
                        findViewById<TextView>(R.id.pressure_mb).text =
                            weather?.current?.pressure_mb.toString()
                        findViewById<TextView>(R.id.text_view_humidity).text =
                            weather?.current?.humidity.toString()

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        } else {
            Toast.makeText(this, "There's no internet connection", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }


}