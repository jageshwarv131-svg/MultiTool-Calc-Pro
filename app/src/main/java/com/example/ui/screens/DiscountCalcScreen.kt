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

@Composable
fun DiscountCalcScreen(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    var activeField by remember { mutableStateOf("price") } // price, disc, tax
    var priceVal by remember { mutableStateOf("159.99") }
    var discVal by remember { mutableStateOf("20") }
    var taxVal by remember { mutableStateOf("10") }

    val formatter = remember {
        DecimalFormat("0.00", DecimalFormatSymbols(Locale.US)).apply { isGroupingUsed = false }
    }

    val price = priceVal.toDoubleOrNull() ?: 0.0
    val discPct = discVal.toDoubleOrNull() ?: 0.0
    val taxPct = taxVal.toDoubleOrNull() ?: 0.0

    val saved = price * discPct / 100.0
    val discountedPrice = price - saved
    val taxAmount = discountedPrice * taxPct / 100.0
    val total = discountedPrice + taxAmount

    fun appendNum(num: String) {
        when (activeField) {
            "price" -> priceVal = if (priceVal == "0") num else if (priceVal.length < 9) priceVal + num else priceVal
            "disc" -> discVal = if (discVal == "0") num else if (discVal.length < 4) discVal + num else discVal
            "tax" -> taxVal = if (taxVal == "0") num else if (taxVal.length < 4) taxVal + num else taxVal
        }
    }

    fun appendDot() {
        when (activeField) {
            "price" -> if (!priceVal.contains(".")) priceVal += "."
            "disc" -> if (!discVal.contains(".")) discVal += "."
            "tax" -> if (!taxVal.contains(".")) taxVal += "."
        }
    }

    fun deleteLast() {
        when (activeField) {
            "price" -> priceVal = if (priceVal.length > 1) priceVal.dropLast(1) else "0"
            "disc" -> discVal = if (discVal.length > 1) discVal.dropLast(1) else "0"
            "tax" -> taxVal = if (taxVal.length > 1) taxVal.dropLast(1) else "0"
        }
    }

    fun clearAll() {
        when (activeField) {
            "price" -> priceVal = "0"
            "disc" -> discVal = "0"
            "tax" -> taxVal = "0"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Form & Summary area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Original Price Card
            GlassCard(
                isSelected = activeField == "price",
                isDark = isDark,
                onClick = { activeField = "price" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(
                        text = "Original Price",
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
                            text = priceVal,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("discount_price_input")
                        )
                        if (activeField == "price") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Discount Card
            GlassCard(
                isSelected = activeField == "disc",
                isDark = isDark,
                onClick = { activeField = "disc" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(
                        text = "Discount",
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
                            text = "$discVal%",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("discount_pct_input")
                        )
                        if (activeField == "disc") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Tax Rate Card
            GlassCard(
                isSelected = activeField == "tax",
                isDark = isDark,
                onClick = { activeField = "tax" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(
                        text = "Tax Rate",
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
                            text = "$taxVal%",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("discount_tax_input")
                        )
                        if (activeField == "tax") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Results Card
            GlassCard(
                isSelected = false,
                isDark = isDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Saved", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155))
                        Text(
                            text = formatter.format(saved),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.testTag("discount_saved_text")
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Price", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155))
                        Text(
                            text = formatter.format(total),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.testTag("discount_total_text")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Standard Keypad
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
