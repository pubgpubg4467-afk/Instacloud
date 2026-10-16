package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Instagram & Liquid Glass Signature Colors
val InstaPurple = Color(0xFF833AB4)
val InstaPink = Color(0xFFFD1D1D)
val InstaOrange = Color(0xFFFCB045)
val InstaYellow = Color(0xFFFFDC80)
val InstaMagenta = Color(0xFFC13584)
val InstaBlue = Color(0xFF00F2FE)
val InstaHeartRed = Color(0xFFFF2A6D)

// Liquid Glass Palette
val InstaDarkBackground = LiquidDeepSpace
val InstaDarkSurface = Color(0x33120E29)
val InstaDarkSurfaceVariant = Color(0x2EFFFFFF)
val InstaDarkBorder = Color(0x3DFFFFFF)
val InstaDarkTextPrimary = Color(0xFFFFFFFF)
val InstaDarkTextSecondary = Color(0xFFB8B5D1)

val InstaLightBackground = Color(0xFF0A0C1B)
val InstaLightSurface = Color(0x33120E29)
val InstaLightSurfaceVariant = Color(0x2EFFFFFF)
val InstaLightBorder = Color(0x3DFFFFFF)
val InstaLightTextPrimary = Color(0xFFFFFFFF)
val InstaLightTextSecondary = Color(0xFFB8B5D1)

val InstaStoryGradient = Brush.sweepGradient(
    colors = listOf(
        Color(0xFF00F2FE),
        Color(0xFFA855F7),
        Color(0xFFFF2A85),
        Color(0xFFFFB300),
        Color(0xFF00F2FE)
    )
)

val InstaStorySeenBrush = Brush.linearGradient(
    colors = listOf(Color(0x66FFFFFF), Color(0x26FFFFFF))
)

val InstaGradient = Brush.linearGradient(
    colors = listOf(InstaPurple, InstaPink, InstaOrange)
)
