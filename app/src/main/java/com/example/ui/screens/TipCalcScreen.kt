package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun TipCalcScreen(
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    var activeField by remember { mutableStateOf("bill") } // bill, people, tip, tax
    var billVal by remember { mutableStateOf("150") }
    var peopleVal by remember { mutableStateOf("3") }
    var tipVal by remember { mutableStateOf("20.00") }
    var tipIsPercent by remember { mutableStateOf(true) }
    var taxVal by remember { mutableStateOf("4.00") }
    var taxIsPercent by remember { mutableStateOf(true) }
    var noTipOnTax by remember { mutableStateOf(false) }

    val formatter = remember {
        DecimalFormat("0.00", DecimalFormatSymbols(Locale.US)).apply { isGroupingUsed = false }
    }

    // Calculations exactly as in original JS
    val bill = billVal.toDoubleOrNull() ?: 0.0
    val people = (peopleVal.toDoubleOrNull() ?: 1.0).coerceAtLeast(1.0)
    val rawTax = taxVal.toDoubleOrNull() ?: 0.0
    val rawTip = tipVal.toDoubleOrNull() ?: 0.0

    val taxAmount = if (taxIsPercent) bill * rawTax / 100.0 else rawTax
    val tipBase = if (noTipOnTax) bill else bill + taxAmount
    val tipAmount = if (tipIsPercent) tipBase * rawTip / 100.0 else rawTip
    val total = bill + taxAmount + tipAmount
    val perPerson = total / people

    fun appendNum(num: String) {
        when (activeField) {
            "bill" -> billVal = if (billVal == "0") num else if (billVal.length < 9) billVal + num else billVal
            "people" -> peopleVal = if (peopleVal == "0") num else if (peopleVal.length < 4) peopleVal + num else peopleVal
            "tip" -> tipVal = if (tipVal == "0") num else if (tipVal.length < 6) tipVal + num else tipVal
            "tax" -> taxVal = if (taxVal == "0") num else if (taxVal.length < 6) taxVal + num else taxVal
        }
    }

    fun appendDot() {
        when (activeField) {
            "bill" -> if (!billVal.contains(".")) billVal += "."
            "people" -> {}
            "tip" -> if (!tipVal.contains(".")) tipVal += "."
            "tax" -> if (!taxVal.contains(".")) taxVal += "."
        }
    }

    fun deleteLast() {
        when (activeField) {
            "bill" -> billVal = if (billVal.length > 1) billVal.dropLast(1) else "0"
            "people" -> peopleVal = if (peopleVal.length > 1) peopleVal.dropLast(1) else "1"
            "tip" -> tipVal = if (tipVal.length > 1) tipVal.dropLast(1) else "0"
            "tax" -> taxVal = if (taxVal.length > 1) taxVal.dropLast(1) else "0"
        }
    }

    fun clearAll() {
        when (activeField) {
            "bill" -> billVal = "0"
            "people" -> peopleVal = "1"
            "tip" -> tipVal = "0"
            "tax" -> taxVal = "0"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Tool Container (Scrollable if needed on small screens)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Box 1: Bill
            GlassCard(
                isSelected = activeField == "bill",
                isDark = isDark,
                onClick = { activeField = "bill" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    Text(
                        text = "Bill",
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
                            text = billVal,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("tip_bill_val")
                        )
                        if (activeField == "bill") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Box 2: People
            GlassCard(
                isSelected = activeField == "people",
                isDark = isDark,
                onClick = { activeField = "people" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    Text(
                        text = "People",
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
                            text = peopleVal,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("tip_people_val")
                        )
                        if (activeField == "people") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Box 3: Tip
            GlassCard(
                isSelected = activeField == "tip",
                isDark = isDark,
                onClick = { activeField = "tip" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tip",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                        )
                        // % / Val toggles
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PillToggle(text = "%", selected = tipIsPercent, isDark = isDark) { tipIsPercent = true }
                            PillToggle(text = "Val", selected = !tipIsPercent, isDark = isDark) { tipIsPercent = false }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tipVal + (if (tipIsPercent) "%" else ""),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("tip_tip_val")
                        )
                        if (activeField == "tip") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Box 4: Tax Rate
            GlassCard(
                isSelected = activeField == "tax",
                isDark = isDark,
                onClick = { activeField = "tax" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tax Rate",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                        )
                        // % / Val toggles
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PillToggle(text = "%", selected = taxIsPercent, isDark = isDark) { taxIsPercent = true }
                            PillToggle(text = "Val", selected = !taxIsPercent, isDark = isDark) { taxIsPercent = false }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = taxVal + (if (taxIsPercent) "%" else ""),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color(0xFF0F172A),
                            modifier = Modifier.testTag("tip_tax_val")
                        )
                        if (activeField == "tax") {
                            Spacer(modifier = Modifier.width(3.dp))
                            BlinkingCursor()
                        }
                    }
                }
            }

            // Box 5: Do not tip on tax switch row
            GlassCard(
                isSelected = false,
                isDark = isDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Do not tip on tax",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) Color.White else Color(0xFF0F172A)
                    )
                    Switch(
                        checked = noTipOnTax,
                        onCheckedChange = { noTipOnTax = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFEF4444)
                        )
                    )
                }
            }

            // Box 6: RESULTS
            GlassCard(
                isSelected = false,
                isDark = isDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "RESULTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155))
                        Text(
                            text = formatter.format(total),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.testTag("tip_res_total")
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Per person", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155))
                        Text(
                            text = formatter.format(perPerson),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.testTag("tip_res_per_person")
                        )
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

@Composable
fun PillToggle(
    text: String,
    selected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) Color(0x33EF4444) else Color.Transparent
    val textColor = if (selected) Color(0xFFEF4444) else (if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B))

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
