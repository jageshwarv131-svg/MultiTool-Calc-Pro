package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class GlassButtonType {
    NUMBER,
    OPERATOR,
    ACTION,
    MEMORY,
    EQUALS,
    BADGE
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: GlassButtonType = GlassButtonType.NUMBER,
    isDark: Boolean = true,
    shape: Shape = CircleShape,
    fontSize: TextUnit = 24.sp,
    testTag: String = "btn_$text",
    icon: (@Composable () -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current

    val backgroundBrush: Brush = when (type) {
        GlassButtonType.EQUALS -> if (isDark) {
            Brush.linearGradient(listOf(Color(0xFFF87171), Color(0xFFDC2626)))
        } else {
            Brush.linearGradient(listOf(Color(0xFFFB7185), Color(0xFFE11D48)))
        }
        GlassButtonType.OPERATOR -> if (isDark) {
            Brush.linearGradient(listOf(Color(0x59EF4444), Color(0x26EF4444)))
        } else {
            Brush.linearGradient(listOf(Color(0xBFFFEBEB), Color(0x73FFDCDC)))
        }
        GlassButtonType.BADGE -> if (isDark) {
            Brush.linearGradient(listOf(Color(0x59EF4444), Color(0x26EF4444)))
        } else {
            Brush.linearGradient(listOf(Color(0xBFFFEBEB), Color(0x73FFDCDC)))
        }
        else -> if (isDark) {
            Brush.linearGradient(listOf(Color(0x2EFFFFFF), Color(0x0FFFFFFF)))
        } else {
            Brush.linearGradient(listOf(Color(0xA6FFFFFF), Color(0x59FFFFFF)))
        }
    }

    val textColor: Color = when (type) {
        GlassButtonType.EQUALS -> Color.White
        GlassButtonType.OPERATOR -> if (isDark) Color(0xFFFCA5A5) else Color(0xFFE11D48)
        GlassButtonType.ACTION -> if (isDark) Color(0xFFF87171) else Color(0xFFE11D48)
        GlassButtonType.MEMORY -> if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155)
        GlassButtonType.BADGE -> if (isDark) Color(0xFFF87171) else Color(0xFFE11D48)
        GlassButtonType.NUMBER -> if (isDark) Color.White else Color(0xFF0F172A)
    }

    val borderColor: Color = when (type) {
        GlassButtonType.EQUALS -> Color(0x66FFFFFF)
        GlassButtonType.OPERATOR -> if (isDark) Color(0x66F87171) else Color(0xCCFFC8C8)
        GlassButtonType.BADGE -> if (isDark) Color(0x66F87171) else Color(0xCCFFC8C8)
        else -> if (isDark) Color(0x40FFFFFF) else Color(0xBFFFFFFF)
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .shadow(
                elevation = if (type == GlassButtonType.EQUALS) 8.dp else 4.dp,
                shape = shape,
                spotColor = if (type == GlassButtonType.EQUALS) Color(0x80E11D48) else Color(0x25000000)
            )
            .clip(shape)
            .background(backgroundBrush)
            .border(1.dp, borderColor, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White.copy(alpha = 0.35f)),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Soft top-shine reflection
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.22f else 0.45f),
                            Color.Transparent
                        )
                    )
                )
        )

        if (icon != null) {
            icon()
        } else {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (type == GlassButtonType.NUMBER) FontWeight.Normal else FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    isSelected: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val bgColor = if (isDark) Color(0xB30F172A) else Color(0xB3FFFFFF)
    val borderColor = if (isSelected) {
        if (isDark) Color(0xFFF87171) else Color(0xFFEF4444)
    } else {
        if (isDark) Color(0x33FFFFFF) else Color(0x80FFFFFF)
    }

    var mod = modifier
        .shadow(
            elevation = if (isSelected) 8.dp else 3.dp,
            shape = shape,
            spotColor = if (isSelected) Color(0x33EF4444) else Color(0x1A000000)
        )
        .clip(shape)
        .background(bgColor)
        .border(if (isSelected) 1.5.dp else 1.dp, borderColor, shape)

    if (onClick != null) {
        mod = mod.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(color = Color.White.copy(alpha = 0.25f)),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
        )
    }

    Box(modifier = mod.padding(1.dp)) {
        content()
    }
}

@Composable
fun BlinkingCursor(
    color: Color = Color(0xFFEF4444),
    width: Dp = 2.dp,
    height: Dp = 22.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(color.copy(alpha = alpha))
    )
}

@Composable
fun GlassTopBar(
    title: String = "",
    onMenuClick: () -> Unit,
    onHistoryClick: () -> Unit,
    showHistory: Boolean,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val iconBg = if (isDark) Color(0x2EFFFFFF) else Color(0xA6FFFFFF)
    val iconBorder = if (isDark) Color(0x40FFFFFF) else Color(0xBFFFFFFF)
    val iconColor = if (isDark) Color.White else Color(0xFF0F172A)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Menu Button
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .size(42.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(iconBg)
                .border(1.dp, iconBorder, CircleShape)
                .testTag("menu_button")
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = iconColor
            )
        }

        if (title.isNotEmpty()) {
            Text(
                text = title.uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = if (isDark) Color.White else Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // History Button
        if (showHistory) {
            IconButton(
                onClick = onHistoryClick,
                modifier = Modifier
                    .size(42.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(iconBg)
                    .border(1.dp, iconBorder, CircleShape)
                    .testTag("history_button")
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Calculation History",
                    tint = iconColor
                )
            }
        } else {
            Spacer(modifier = Modifier.size(42.dp))
        }
    }
}
