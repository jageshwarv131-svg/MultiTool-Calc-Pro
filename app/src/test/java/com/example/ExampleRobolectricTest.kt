package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.ExpressionEvaluator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MultiTool Calc Pro", appName)
    }

    @Test
    fun `evaluate basic math expressions`() {
        val res1 = ExpressionEvaluator.evaluate("12 + 8 × 2").getOrThrow()
        assertEquals(28.0, res1, 0.001)

        val res2 = ExpressionEvaluator.evaluate("100 ÷ 4 - 5").getOrThrow()
        assertEquals(20.0, res2, 0.001)
    }

    @Test
    fun `evaluate scientific trigonometric and log expressions`() {
        val resSin = ExpressionEvaluator.evaluate("sin(0)", isRadian = true).getOrThrow()
        assertEquals(0.0, resSin, 0.001)

        val resAutoClose = ExpressionEvaluator.evaluate("sin(0").getOrThrow()
        assertEquals(0.0, resAutoClose, 0.001)

        val resSqrt = ExpressionEvaluator.evaluate("√(144)").getOrThrow()
        assertEquals(12.0, resSqrt, 0.001)
    }
}
