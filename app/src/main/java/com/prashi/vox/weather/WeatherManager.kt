package com.prashi.vox.weather

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class WeatherManager {

    fun getWeather(
        lat: Double,
        lon: Double,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        Thread {

            try {

                val apiKey = "a03a3a57ef0cf99c536393f48326becc"

                val url = URL(
                    "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric"
                )

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                val response =
                    connection.inputStream.bufferedReader().readText()

                val json = JSONObject(response)

                val city = json.getString("name")

                val temp = json
                    .getJSONObject("main")
                    .getDouble("temp")

                val weather = json
                    .getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description")

                val message =
                    "Current temperature in $city is $temp degree Celsius with $weather"

                onSuccess(message)

            } catch (e: Exception) {

                onError(e.toString())
            }

        }.start()
    }
}