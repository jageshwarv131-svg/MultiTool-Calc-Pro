package com.example.engine

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.*

object ExpressionEvaluator {

    private val decimalFormat = DecimalFormat("0.########", DecimalFormatSymbols(Locale.US)).apply {
        isGroupingUsed = false
    }

    fun formatResult(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        if (abs(value) < 1e-12) return "0"
        if (abs(value) >= 1e12 || (abs(value) > 0 && abs(value) < 1e-6)) {
            val sciFormat = DecimalFormat("0.######E0", DecimalFormatSymbols(Locale.US))
            return sciFormat.format(value)
        }
        val rounded = (value * 100000000.0).roundToLong() / 100000000.0
        return decimalFormat.format(rounded)
    }

    fun evaluate(expression: String, isRadian: Boolean = true): Result<Double> {
        return try {
            var sanitized = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace("π", Math.PI.toString())
                .replace("e", Math.E.toString())
                .trim()

            if (sanitized.isEmpty()) return Result.success(0.0)

            // Auto-close missing parentheses
            val openCount = sanitized.count { it == '(' }
            val closeCount = sanitized.count { it == ')' }
            if (openCount > closeCount) {
                sanitized += ")".repeat(openCount - closeCount)
            }

            val parser = Parser(sanitized, isRadian)
            val result = parser.parse()
            if (result.isNaN() || result.isInfinite()) {
                Result.failure(ArithmeticException("Undefined"))
            } else {
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private class Parser(private val src: String, private val isRadian: Boolean) {
        private var pos = 0
        private val len = src.length

        fun parse(): Double {
            val v = parseExpression()
            skipWhitespace()
            return v
        }

        private fun peek(): Char = if (pos < len) src[pos] else '\u0000'
        private fun next(): Char = if (pos < len) src[pos++] else '\u0000'
        private fun skipWhitespace() {
            while (pos < len && src[pos].isWhitespace()) pos++
        }

        private fun parseExpression(): Double {
            var left = parseTerm()
            while (true) {
                skipWhitespace()
                when (peek()) {
                    '+' -> {
                        next()
                        left += parseTerm()
                    }
                    '-' -> {
                        next()
                        left -= parseTerm()
                    }
                    else -> return left
                }
            }
        }

        private fun parseTerm(): Double {
            var left = parseFactor()
            while (true) {
                skipWhitespace()
                when (peek()) {
                    '*' -> {
                        next()
                        left *= parseFactor()
                    }
                    '/' -> {
                        next()
                        val right = parseFactor()
                        if (abs(right) < 1e-15) throw ArithmeticException("Divide by zero")
                        left /= right
                    }
                    '%' -> {
                        next()
                        val right = parseFactor()
                        if (right != 0.0) {
                            left %= right
                        } else {
                            left /= 100.0
                        }
                    }
                    else -> return left
                }
            }
        }

        private fun parseFactor(): Double {
            var base = parseUnary()
            skipWhitespace()
            if (peek() == '^') {
                next()
                val exponent = parseFactor()
                base = base.pow(exponent)
            }
            skipWhitespace()
            if (peek() == '%') {
                // Check if postfix percent
                next()
                base /= 100.0
            }
            return base
        }

        private fun parseUnary(): Double {
            skipWhitespace()
            if (peek() == '+') {
                next()
                return parseUnary()
            }
            if (peek() == '-') {
                next()
                return -parseUnary()
            }
            return parsePrimary()
        }

        private fun parsePrimary(): Double {
            skipWhitespace()
            val c = peek()

            if (c == '(') {
                next()
                val res = parseExpression()
                skipWhitespace()
                if (peek() == ')') next()
                return res
            }

            if (c.isDigit() || c == '.') {
                val start = pos
                var hasDot = false
                while (pos < len) {
                    val ch = src[pos]
                    if (ch == '.') {
                        if (hasDot) break
                        hasDot = true
                        pos++
                    } else if (ch.isDigit()) {
                        pos++
                    } else if ((ch == 'E' || ch == 'e') && pos + 1 < len) {
                        val nextCh = src[pos + 1]
                        if (nextCh == '+' || nextCh == '-' || nextCh.isDigit()) {
                            pos += 2
                            while (pos < len && src[pos].isDigit()) pos++
                            break
                        } else {
                            break
                        }
                    } else {
                        break
                    }
                }
                val numStr = src.substring(start, pos)
                return numStr.toDoubleOrNull() ?: 0.0
            }

            if (c.isLetter() || c == '√') {
                val start = pos
                while (pos < len && (src[pos].isLetter() || src[pos] == '√')) {
                    pos++
                }
                val name = src.substring(start, pos)
                skipWhitespace()
                val arg = if (peek() == '(') {
                    next()
                    val a = parseExpression()
                    skipWhitespace()
                    if (peek() == ')') next()
                    a
                } else {
                    parseFactor()
                }

                return when (name.lowercase()) {
                    "sin" -> {
                        val angle = if (isRadian) arg else Math.toRadians(arg)
                        sin(angle)
                    }
                    "cos" -> {
                        val angle = if (isRadian) arg else Math.toRadians(arg)
                        cos(angle)
                    }
                    "tan" -> {
                        val angle = if (isRadian) arg else Math.toRadians(arg)
                        tan(angle)
                    }
                    "asin" -> {
                        val r = asin(arg)
                        if (isRadian) r else Math.toDegrees(r)
                    }
                    "acos" -> {
                        val r = acos(arg)
                        if (isRadian) r else Math.toDegrees(r)
                    }
                    "atan" -> {
                        val r = atan(arg)
                        if (isRadian) r else Math.toDegrees(r)
                    }
                    "sinh" -> sinh(arg)
                    "cosh" -> cosh(arg)
                    "tanh" -> tanh(arg)
                    "lg", "log" -> log10(arg)
                    "ln" -> ln(arg)
                    "√", "sqrt" -> sqrt(arg)
                    else -> arg
                }
            }

            if (pos < len) next()
            return 0.0
        }
    }
}
