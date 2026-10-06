package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ExpressionEvaluator
import com.example.model.CalculationRecord
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassButtonType

@Composable
fun ScientificCalcScreen(
    isDark: Boolean,
    onCalculationSuccess: (CalculationRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayValue by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var isRadianMode by remember { mutableStateOf(true) }
    var isInvMode by remember { mutableStateOf(false) }
    var shouldReset by remember { mutableStateOf(false) }

    fun appendNum(num: String) {
        displayValue = if (displayValue == "0" || shouldReset) num else displayValue + num
        shouldReset = false
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

    fun appendChar(char: String) {
        displayValue = if (displayValue == "0" && char != "-") char else "$displayValue $char "
        shouldReset = false
    }

    fun appendFunc(f: String) {
        displayValue = if (displayValue == "0" || shouldReset) f else displayValue + f
        shouldReset = false
    }

    fun appendConst(c: String) {
        displayValue = if (displayValue == "0" || shouldReset) c else displayValue + c
        shouldReset = false
    }

    fun smartBracket() {
        val openCount = displayValue.count { it == '(' }
        val closeCount = displayValue.count { it == ')' }
        val bracket = if (openCount > closeCount && displayValue.isNotEmpty() &&
            (displayValue.last().isDigit() || displayValue.last() == ')' || displayValue.last() == 'π' || displayValue.last() == 'e')
        ) {
            ")"
        } else {
            "("
        }
        displayValue = if (displayValue == "0" || shouldReset) bracket else displayValue + bracket
        shouldReset = false
    }

    fun clearAll() {
        displayValue = "0"
        expression = ""
        shouldReset = false
    }

    fun deleteLast() {
        val trimmed = displayValue.trimEnd()
        displayValue = if (trimmed.length > 1) {
            trimmed.dropLast(1).trimEnd().ifEmpty { "0" }
        } else {
            "0"
        }
    }

    fun calculate() {
        val expr = displayValue
        val result = ExpressionEvaluator.evaluate(expr, isRadian = isRadianMode)
        if (result.isSuccess) {
            val formatted = ExpressionEvaluator.formatResult(result.getOrThrow())
            onCalculationSuccess(CalculationRecord(expression = expr, result = formatted))
            expression = "$expr ="
            displayValue = formatted
            shouldReset = true
        } else {
            displayValue = "Error"
            shouldReset = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression,
                fontSize = 18.sp,
                color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sci_expression_text")
            )

            Spacer(modifier = Modifier.height(4.dp))

            val fontSize = when {
                displayValue.length > 14 -> 28.sp
                displayValue.length > 9 -> 38.sp
                else -> 50.sp
            }

            Text(
                text = displayValue,
                fontSize = fontSize,
                lineHeight = fontSize,
                fontWeight = FontWeight.Normal,
                color = if (isDark) Color.White else Color(0xFF0F172A),
                maxLines = 2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sci_display_text")
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // 5-Column Scientific Keypad
        val spacing = 6.dp
        val sciFontSize = 14.sp
        val numFontSize = 20.sp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            // Row 1: ⇄, Rad/Deg, sin, cos, tan
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                GlassButton(
                    text = "⇄",
                    onClick = { isInvMode = !isInvMode },
                    type = if (isInvMode) GlassButtonType.BADGE else GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = if (isRadianMode) "Rad" else "Deg",
                    onClick = { isRadianMode = !isRadianMode },
                    type = GlassButtonType.BADGE,
                    isDark = isDark,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = if (isInvMode) "asin" else "sin",
                    onClick = { appendFunc(if (isInvMode) "asin(" else "sin(") },
                    type = GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = sciFontSize,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = if (isInvMode) "acos" else "cos",
                    onClick = { appendFunc(if (isInvMode) "acos(" else "cos(") },
                    type = GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = sciFontSize,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
                GlassButton(
                    text = if (isInvMode) "atan" else "tan",
                    onClick = { appendFunc(if (isInvMode) "atan(" else "tan(") },
                    type = GlassButtonType.MEMORY,
                    isDark = isDark,
                    fontSize = sciFontSize,
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }

            // Row 2: e, π, sinh, cosh, tanh
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                GlassButton(text = "e", onClick = { appendConst("e") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = 16.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "π", onClick = { appendConst("π") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = 16.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "sinh", onClick = { appendFunc("sinh(") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "cosh", onClick = { appendFunc("cosh(") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "tanh", onClick = { appendFunc("tanh(") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
            }

            // Row 3: %, AC, ÷, ×, ⌫
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                GlassButton(text = "%", onClick = { appendChar("%") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = 16.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "AC", onClick = { clearAll() }, type = GlassButtonType.ACTION, isDark = isDark, fontSize = 16.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "÷", onClick = { appendChar("÷") }, type = GlassButtonType.OPERATOR, isDark = isDark, fontSize = 22.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "×", onClick = { appendChar("×") }, type = GlassButtonType.OPERATOR, isDark = isDark, fontSize = 22.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "⌫", onClick = { deleteLast() }, type = GlassButtonType.ACTION, isDark = isDark, fontSize = 18.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }

            // Row 4: ( ), 7, 8, 9, -
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                GlassButton(text = "( )", onClick = { smartBracket() }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "7", onClick = { appendNum("7") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "8", onClick = { appendNum("8") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "9", onClick = { appendNum("9") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "-", onClick = { appendChar("-") }, type = GlassButtonType.OPERATOR, isDark = isDark, fontSize = 24.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }

            // Row 5: 1/x, 4, 5, 6, +
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                GlassButton(text = "1/x", onClick = { appendFunc("1/(") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "4", onClick = { appendNum("4") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "5", onClick = { appendNum("5") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "6", onClick = { appendNum("6") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                GlassButton(text = "+", onClick = { appendChar("+") }, type = GlassButtonType.OPERATOR, isDark = isDark, fontSize = 22.sp, modifier = Modifier.weight(1f).aspectRatio(1f))
            }

            // Rows 6 & 7: 4 columns on left + 1 column on right for double-height '=' button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                // Left 4 columns
                Column(
                    modifier = Modifier.weight(4f),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    // Row 6: √x, 1, 2, 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        GlassButton(text = "√x", onClick = { appendFunc("√(") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                        GlassButton(text = "1", onClick = { appendNum("1") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                        GlassButton(text = "2", onClick = { appendNum("2") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                        GlassButton(text = "3", onClick = { appendNum("3") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                    }

                    // Row 7: lg, 00, 0, .
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        GlassButton(text = "lg", onClick = { appendFunc("lg(") }, type = GlassButtonType.MEMORY, isDark = isDark, fontSize = sciFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                        GlassButton(text = "00", onClick = { appendNum("00") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                        GlassButton(text = "0", onClick = { appendNum("0") }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                        GlassButton(text = ".", onClick = { appendDot() }, isDark = isDark, fontSize = numFontSize, modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }

                // 5th Column: '=' button spanning both rows with rounded pill shape
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    GlassButton(
                        text = "=",
                        onClick = { calculate() },
                        type = GlassButtonType.EQUALS,
                        isDark = isDark,
                        shape = RoundedCornerShape(26.dp),
                        fontSize = 26.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
