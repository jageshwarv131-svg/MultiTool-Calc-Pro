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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ExpressionEvaluator
import com.example.model.CalculationRecord
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassButtonType

@Composable
fun StandardCalcScreen(
    isDark: Boolean,
    onCalculationSuccess: (CalculationRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayValue by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var memoryValue by remember { mutableDoubleStateOf(0.0) }
    var shouldReset by remember { mutableStateOf(false) }

    fun appendNumber(num: String) {
        if (displayValue == "0" || shouldReset) {
            displayValue = num
            shouldReset = false
        } else if (displayValue.length < 15) {
            displayValue += num
        }
    }

    fun appendDot() {
        if (shouldReset) {
            displayValue = "0"
            shouldReset = false
        }
        if (!displayValue.contains(".")) {
            displayValue += "."
        }
    }

    fun appendOperator(op: String) {
        shouldReset = false
        expression += "$displayValue $op "
        displayValue = "0"
    }

    fun clearAll() {
        displayValue = "0"
        expression = ""
        shouldReset = false
    }

    fun deleteLast() {
        displayValue = if (displayValue.length > 1) {
            displayValue.dropLast(1)
        } else {
            "0"
        }
    }

    fun toggleSign() {
        if (displayValue != "0") {
            displayValue = if (displayValue.startsWith("-")) {
                displayValue.removePrefix("-")
            } else {
                "-$displayValue"
            }
        }
    }

    fun calculatePercentage() {
        val v = displayValue.toDoubleOrNull() ?: return
        displayValue = ExpressionEvaluator.formatResult(v / 100.0)
    }

    fun calculate() {
        if (expression.isEmpty() && displayValue.isNotEmpty()) return
        val fullExpr = expression + displayValue
        val result = ExpressionEvaluator.evaluate(fullExpr)
        if (result.isSuccess) {
            val formatted = ExpressionEvaluator.formatResult(result.getOrThrow())
            onCalculationSuccess(CalculationRecord(expression = fullExpr, result = formatted))
            displayValue = formatted
            expression = ""
            shouldReset = true
        } else {
            displayValue = "Error"
            shouldReset = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression,
                fontSize = 20.sp,
                color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("std_expression_text")
            )

            Spacer(modifier = Modifier.height(4.dp))

            val fontSize = when {
                displayValue.length > 11 -> 36.sp
                displayValue.length > 8 -> 48.sp
                else -> 60.sp
            }

            Text(
                text = displayValue,
                fontSize = fontSize,
                lineHeight = fontSize,
                fontWeight = FontWeight.Normal,
                color = if (isDark) Color.White else Color(0xFF0F172A),
                maxLines = 1,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("std_display_text")
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Standard Keypad: 4 columns of perfect circular glass buttons
        val buttonSpacing = 10.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            // Memory Row: mc, m+, m-, mr
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                GlassButton(
                    text = "mc",
                    onClick = { memoryValue = 0.0 },
                    type = GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = 19.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = "m+",
                    onClick = {
                        memoryValue += (displayValue.toDoubleOrNull() ?: 0.0)
                        shouldReset = true
                    },
                    type = GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = 19.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = "m-",
                    onClick = {
                        memoryValue -= (displayValue.toDoubleOrNull() ?: 0.0)
                        shouldReset = true
                    },
                    type = GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = 19.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = "mr",
                    onClick = {
                        displayValue = ExpressionEvaluator.formatResult(memoryValue)
                        shouldReset = true
                    },
                    type = GlassButtonType.ACTION,
                    isDark = isDark,
                    fontSize = 19.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }

            // Row 1: AC, ⌫, +/-, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                GlassButton(
                    text = "AC",
                    onClick = { clearAll() },
                    type = GlassButtonType.ACTION,
                    isDark = isDark,
                    fontSize = 22.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = "⌫",
                    onClick = { deleteLast() },
                    type = GlassButtonType.ACTION,
                    isDark = isDark,
                    fontSize = 22.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = "+/-",
                    onClick = { toggleSign() },
                    type = GlassButtonType.ACTION,
                    isDark = isDark,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = "÷",
                    onClick = { appendOperator("÷") },
                    type = GlassButtonType.OPERATOR,
                    isDark = isDark,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                GlassButton(text = "7", onClick = { appendNumber("7") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "8", onClick = { appendNumber("8") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "9", onClick = { appendNumber("9") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(
                    text = "×",
                    onClick = { appendOperator("×") },
                    type = GlassButtonType.OPERATOR,
                    isDark = isDark,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }

            // Row 3: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                GlassButton(text = "4", onClick = { appendNumber("4") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "5", onClick = { appendNumber("5") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "6", onClick = { appendNumber("6") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(
                    text = "-",
                    onClick = { appendOperator("-") },
                    type = GlassButtonType.OPERATOR,
                    isDark = isDark,
                    fontSize = 30.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                GlassButton(text = "1", onClick = { appendNumber("1") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "2", onClick = { appendNumber("2") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "3", onClick = { appendNumber("3") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(
                    text = "+",
                    onClick = { appendOperator("+") },
                    type = GlassButtonType.OPERATOR,
                    isDark = isDark,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }

            // Row 5: %, 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                GlassButton(
                    text = "%",
                    onClick = { calculatePercentage() },
                    type = GlassButtonType.NUMBER,
                    isDark = isDark,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(text = "0", onClick = { appendNumber("0") }, isDark = isDark, fontSize = 26.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = ".", onClick = { appendDot() }, isDark = isDark, fontSize = 28.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(
                    text = "=",
                    onClick = { calculate() },
                    type = GlassButtonType.EQUALS,
                    isDark = isDark,
                    fontSize = 30.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }
        }
    }
}
