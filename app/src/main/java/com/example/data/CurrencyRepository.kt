package com.example.data

import com.example.model.Currencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class ExchangeRatesResult(
    val rates: Map<String, Double>,
    val lastUpdated: String,
    val isLive: Boolean
)

class CurrencyRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun fetchRates(): ExchangeRatesResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://open.er-api.com/v6/latest/USD")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    if (!body.isNullOrBlank()) {
                        val json = JSONObject(body)
                        val ratesJson = json.optJSONObject("rates")
                        if (ratesJson != null) {
                            val ratesMap = mutableMapOf<String, Double>()
                            val keys = ratesJson.keys()
                            while (keys.hasNext()) {
                                val key = keys.next()
                                ratesMap[key] = ratesJson.optDouble(key, 1.0)
                            }
                            val sdf = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
                            val timeStr = sdf.format(Date())
                            return@withContext ExchangeRatesResult(
                                rates = ratesMap,
                                lastUpdated = timeStr,
                                isLive = true
                            )
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Network failure or timeout, fallback to default offline rates
        }

        ExchangeRatesResult(
            rates = Currencies.defaultRates,
            lastUpdated = "Offline Base",
            isLive = false
        )
    }
}
