package com.example.model

data class CurrencyItem(
    val code: String,
    val name: String,
    val flag: String,
    val symbol: String = ""
)

object Currencies {
    val items = listOf(
        CurrencyItem("USD", "United States Dollar", "🇺🇸", "$"),
        CurrencyItem("INR", "Indian Rupee", "🇮🇳", "₹"),
        CurrencyItem("EUR", "Euro", "🇪🇺", "€"),
        CurrencyItem("GBP", "British Pound", "🇬🇧", "£"),
        CurrencyItem("AED", "UAE Dirham", "🇦🇪", "د.إ"),
        CurrencyItem("AUD", "Australian Dollar", "🇦🇺", "A$"),
        CurrencyItem("CAD", "Canadian Dollar", "🇨🇦", "C$"),
        CurrencyItem("SGD", "Singapore Dollar", "🇸🇬", "S$"),
        CurrencyItem("JPY", "Japanese Yen", "🇯🇵", "¥"),
        CurrencyItem("CNY", "Chinese Yuan", "🇨🇳", "¥"),
        CurrencyItem("SAR", "Saudi Riyal", "🇸🇦", "﷼"),
        CurrencyItem("KRW", "South Korean Won", "🇰🇷", "₩"),
        CurrencyItem("RUB", "Russian Ruble", "🇷🇺", "₽"),
        CurrencyItem("BRL", "Brazilian Real", "🇧🇷", "R$"),
        CurrencyItem("ZAR", "South African Rand", "🇿🇦", "R"),
        CurrencyItem("CHF", "Swiss Franc", "🇨🇭", "CHF"),
        CurrencyItem("HKD", "Hong Kong Dollar", "🇭🇰", "HK$"),
        CurrencyItem("NZD", "New Zealand Dollar", "🇳🇿", "NZ$"),
        CurrencyItem("MXN", "Mexican Peso", "🇲🇽", "$"),
        CurrencyItem("THB", "Thai Baht", "🇹🇭", "฿")
    )

    val defaultRates = mapOf(
        "USD" to 1.0,
        "INR" to 83.25,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "AED" to 3.67,
        "AUD" to 1.52,
        "CAD" to 1.36,
        "SGD" to 1.34,
        "JPY" to 151.5,
        "CNY" to 7.23,
        "SAR" to 3.75,
        "KRW" to 1350.0,
        "RUB" to 92.5,
        "BRL" to 5.05,
        "ZAR" to 18.6,
        "CHF" to 0.90,
        "HKD" to 7.82,
        "NZD" to 1.66,
        "MXN" to 16.7,
        "THB" to 36.5
    )
}
