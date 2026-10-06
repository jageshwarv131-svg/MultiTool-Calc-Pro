package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CalculationRecord
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("multitool_calc_prefs", Context.MODE_PRIVATE)

    var wallpaperIndex: Int
        get() = prefs.getInt(KEY_WALLPAPER_INDEX, 0)
        set(value) = prefs.edit().putInt(KEY_WALLPAPER_INDEX, value).apply()

    var isDarkModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, true)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var customWallpaperUri: String?
        get() = prefs.getString(KEY_CUSTOM_WALLPAPER_URI, null)
        set(value) = prefs.edit().putString(KEY_CUSTOM_WALLPAPER_URI, value).apply()

    fun getHistory(isScientific: Boolean): List<CalculationRecord> {
        val key = if (isScientific) KEY_HISTORY_SCI else KEY_HISTORY_STD
        val jsonStr = prefs.getString(key, null) ?: return emptyList()
        val list = mutableListOf<CalculationRecord>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    CalculationRecord(
                        expression = obj.optString("expression", ""),
                        result = obj.optString("result", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
        } catch (_: Exception) { }
        return list
    }

    fun addHistoryItem(isScientific: Boolean, record: CalculationRecord) {
        val current = getHistory(isScientific).toMutableList()
        current.add(0, record)
        if (current.size > 50) {
            current.removeAt(current.lastIndex)
        }
        val jsonArray = JSONArray()
        for (item in current) {
            val obj = JSONObject().apply {
                put("expression", item.expression)
                put("result", item.result)
                put("timestamp", item.timestamp)
            }
            jsonArray.put(obj)
        }
        val key = if (isScientific) KEY_HISTORY_SCI else KEY_HISTORY_STD
        prefs.edit().putString(key, jsonArray.toString()).apply()
    }

    fun clearHistory(isScientific: Boolean) {
        val key = if (isScientific) KEY_HISTORY_SCI else KEY_HISTORY_STD
        prefs.edit().remove(key).apply()
    }

    companion object {
        private const val KEY_WALLPAPER_INDEX = "smart_calc_wallpaper_idx"
        private const val KEY_DARK_MODE = "smart_calc_dark_mode"
        private const val KEY_CUSTOM_WALLPAPER_URI = "smart_calc_custom_wallpaper"
        private const val KEY_HISTORY_STD = "smart_calc_history_std"
        private const val KEY_HISTORY_SCI = "smart_calc_history_sci"
    }
}
