package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurrencyRepository
import com.example.model.Currencies
import com.example.ui.components.BlinkingCursor
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassButtonType
import com.example.ui.components.GlassCard
import com.example.ui.dialogs.CurrencyPickerSheet
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToLong

@Composable
fun CurrencyConverterScreen(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val repository = remember { CurrencyRepository() }

    var ratesMap by remember { mutableStateOf(Currencies.defaultRates) }
    var rateUpdateTime by remember { mutableStateOf("Loading...") }

    var currency1 by remember { mutableStateOf(Currencies.items[0]) } // USD
    var currency2 by remember { mutableStateOf(Currencies.items[2]) } // EUR

    var activeBox by remember { mutableIntStateOf(1) }
    var val1 by remember { mutableStateOf("100") }
    var val2 by remember { mutableStateOf("92") }

    var showPickerForBox by remember { mutableStateOf<Int?>(null) }

    val formatter = remember {
        DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).apply { isGroupingUsed = false }
    }

    fun recalculate() {
        val r1 = ratesMap[currency1.code] ?: 1.0
        val r2 = ratesMap[currency2.code] ?: 1.0
        val factor = r2 / r1

        if (activeBox == 1) {
            val amount1 = val1.toDoubleOrNull() ?: 0.0
            val converted = (amount1 * factor * 100.0).roundToLong() / 100.0
            val2 = formatter.format(converted)
        } else {
            val amount2 = val2.toDoubleOrNull() ?: 0.0
            val converted = if (factor > 0) (amount2 / factor * 100.0).roundToLong() / 100.0 else 0.0
            val1 = formatter.format(converted)
        }
    }

    LaunchedEffect(Unit) {
        val result = repository.fetchRates()
        ratesMap = result.rates
        rateUpdateTime = result.lastUpdated
        recalculate()
    }

    fun appendNum(num: String) {
        if (activeBox == 1) {
            val1 = if (val1 == "0") num else if (val1.length < 12) val1 + num else val1
        } else {
            val2 = if (val2 == "0") num else if (val2.length < 12) val2 + num else val2
        }
        recalculate()
    }

    fun appendDot() {
        if (activeBox == 1) {
            if (!val1.contains(".")) val1 += "."
        } else {
            if (!val2.contains(".")) val2 += "."
        }
        recalculate()
    }

    fun deleteLast() {
        if (activeBox == 1) {
            val1 = if (val1.length > 1) val1.dropLast(1) else "0"
        } else {
            val2 = if (val2.length > 1) val2.dropLast(1) else "0"
        }
        recalculate()
    }

    fun clearAll() {
        if (activeBox == 1) val1 = "0" else val2 = "0"
        recalculate()
    }

    fun swapCurrencies() {
        val tempCurr = currency1
        currency1 = currency2
        currency2 = tempCurr

        val tempVal = val1
        val1 = val2
        val2 = tempVal

        recalculate()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Converter Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 1
            GlassCard(
                isSelected = activeBox == 1,
                isDark = isDark,
                onClick = { activeBox = 1 },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPickerForBox = 1 },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = currency1.flag, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currency1.code,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color(0xFF0F172A)
                            )
                        }
                        Text(text = "∨", fontSize = 15.sp, color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = val1,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("currency_val1")
                        )
                        if (activeBox == 1) {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Card 2
            GlassCard(
                isSelected = activeBox == 2,
                isDark = isDark,
                onClick = { activeBox = 2 },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPickerForBox = 2 },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = currency2.flag, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currency2.code,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color(0xFF0F172A)
                            )
                        }
                        Text(text = "∨", fontSize = 15.sp, color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = val2,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("currency_val2")
                        )
                        if (activeBox == 2) {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Rate info
            val r1 = ratesMap[currency1.code] ?: 1.0
            val r2 = ratesMap[currency2.code] ?: 1.0
            val ratio = if (r1 > 0) r2 / r1 else 1.0
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💰 1 ${currency1.code} ≈ ${formatter.format(ratio)} ${currency2.code}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color.White else Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Rate Updated: $rateUpdateTime",
                    fontSize = 11.sp,
                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)
                )
            }
        }

        // Standard Keypad: 4 columns of circular buttons
        val buttonSpacing = 10.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            // Row 1: 7, 8, 9, ⌫
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "7", onClick = { appendNum("7") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "8", onClick = { appendNum("8") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "9", onClick = { appendNum("9") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "⌫", onClick = { deleteLast() }, type = GlassButtonType.ACTION, isDark = isDark, fontSize = 22.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }
            // Row 2: 4, 5, 6, ⇅
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "4", onClick = { appendNum("4") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "5", onClick = { appendNum("5") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "6", onClick = { appendNum("6") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "⇅", onClick = { swapCurrencies() }, type = GlassButtonType.OPERATOR, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }
            // Row 3: 1, 2, 3, AC
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "1", onClick = { appendNum("1") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "2", onClick = { appendNum("2") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "3", onClick = { appendNum("3") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "AC", onClick = { clearAll() }, type = GlassButtonType.ACTION, isDark = isDark, fontSize = 20.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }
            // Row 4: 00, 0, ., ✓
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "00", onClick = { appendNum("00") }, isDark = isDark, fontSize = 20.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "0", onClick = { appendNum("0") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = ".", onClick = { appendDot() }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "✓", onClick = { /* Done */ }, type = GlassButtonType.EQUALS, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }
        }
    }

    // Picker Bottom Sheet
    if (showPickerForBox != null) {
        CurrencyPickerSheet(
            isDark = isDark,
            onCurrencySelected = { selected ->
                if (showPickerForBox == 1) {
                    currency1 = selected
                } else {
                    currency2 = selected
                }
                recalculate()
            },
            onDismiss = { showPickerForBox = null }
        )
    }
}
