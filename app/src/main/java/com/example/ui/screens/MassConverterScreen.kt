package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.ui.components.BlinkingCursor
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassButtonType
import com.example.ui.components.GlassCard
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToLong

data class MassUnit(val key: String, val name: String, val factor: Double)

@Composable
fun MassConverterScreen(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val units = remember {
        listOf(
            MassUnit("kg", "Kilogram (kg)", 1.0),
            MassUnit("lb", "Pound (lb)", 2.20462262185),
            MassUnit("g", "Gram (g)", 1000.0),
            MassUnit("oz", "Ounce (oz)", 35.27396195)
        )
    }

    var activeKey by remember { mutableStateOf("kg") }
    val valuesMap = remember {
        mutableStateMapOf(
            "kg" to "1",
            "lb" to "2.2046",
            "g" to "1000",
            "oz" to "35.274"
        )
    }

    val formatter = remember {
        DecimalFormat("0.####", DecimalFormatSymbols(Locale.US)).apply { isGroupingUsed = false }
    }

    fun recalculate() {
        val currentUnit = units.first { it.key == activeKey }
        val inputVal = valuesMap[activeKey]?.toDoubleOrNull() ?: 0.0
        val baseKg = inputVal / currentUnit.factor

        for (u in units) {
            if (u.key != activeKey) {
                val converted = (baseKg * u.factor * 10000.0).roundToLong() / 10000.0
                valuesMap[u.key] = formatter.format(converted)
            }
        }
    }

    fun appendNum(num: String) {
        val current = valuesMap[activeKey] ?: "0"
        valuesMap[activeKey] = if (current == "0") num else if (current.length < 10) current + num else current
        recalculate()
    }

    fun appendDot() {
        val current = valuesMap[activeKey] ?: "0"
        if (!current.contains(".")) {
            valuesMap[activeKey] = "$current."
        }
        recalculate()
    }

    fun deleteLast() {
        val current = valuesMap[activeKey] ?: "0"
        valuesMap[activeKey] = if (current.length > 1) current.dropLast(1) else "0"
        recalculate()
    }

    fun clearAll() {
        valuesMap[activeKey] = "0"
        recalculate()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 4 Mass Unit Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            units.forEach { unit ->
                val isSelected = activeKey == unit.key
                GlassCard(
                    isSelected = isSelected,
                    isDark = isDark,
                    onClick = { activeKey = unit.key },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(
                            text = unit.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = valuesMap[unit.key] ?: "0",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) Color.White else Color(0xFF0F172A),
                                modifier = Modifier.testTag("mass_val_${unit.key}")
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(3.dp))
                                BlinkingCursor()
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Standard Keypad: 4 columns
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
            // Row 2: 4, 5, 6, AC
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "4", onClick = { appendNum("4") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "5", onClick = { appendNum("5") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "6", onClick = { appendNum("6") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "AC", onClick = { clearAll() }, type = GlassButtonType.ACTION, isDark = isDark, fontSize = 20.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }
            // Row 3: 1, 2, 3, 00
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "1", onClick = { appendNum("1") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "2", onClick = { appendNum("2") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "3", onClick = { appendNum("3") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "00", onClick = { appendNum("00") }, isDark = isDark, fontSize = 20.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }
            // Row 4: 0, ., ✓ (✓ spans 2 columns)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                GlassButton(text = "0", onClick = { appendNum("0") }, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = ".", onClick = { appendDot() }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(
                    text = "✓",
                    onClick = { /* Done */ },
                    type = GlassButtonType.EQUALS,
                    isDark = isDark,
                    shape = RoundedCornerShape(28.dp),
                    fontSize = 24.sp,
                    modifier = Modifier.weight(2f).aspectRatio(2.2f)
                )
            }
        }
    }
}
