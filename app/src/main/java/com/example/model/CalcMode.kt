package com.example.model

enum class CalcMode(val title: String) {
    STANDARD("Standard Calculator"),
    SCIENTIFIC("Scientific Calculator"),
    CURRENCY("Currency Converter"),
    MASS("Mass Converter"),
    TIP("Tip Calculator"),
    DISCOUNT("Discount Calculator")
}

data class CalculationRecord(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
