package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// --- Liquid Glass Palette ---
val LiquidDeepSpace = Color(0xFF090A14)
val LiquidDarkViolet = Color(0xFF130924)
val LiquidDarkIndigo = Color(0xFF0D122B)

// Neon Liquid Accents
val LiquidCyan = Color(0xFF00F2FE)
val LiquidElectricBlue = Color(0xFF4FACFE)
val LiquidNeonPink = Color(0xFFFF2A85)
val LiquidNeonPurple = Color(0xFFA855F7)
val LiquidAmber = Color(0xFFFFB300)

// Glass Surfaces
val GlassFillLight = Color(0x24FFFFFF)
val GlassFillMedium = Color(0x1AFFFFFF)
val GlassFillSubtle = Color(0x0FFFFFFF)
val GlassFillDark = Color(0x55110E24)
val GlassFillCard = Color(0x381A1633)

// Specular Glass Edge Borders
val GlassBorderTopLeft = Color(0x80FFFFFF)
val GlassBorderBottomRight = Color(0x1AFFFFFF)
val GlassBorderCyan = Color(0x4000F2FE)
val GlassBorderPink = Color(0x40FF2A85)

val LiquidGlassBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color(0x80FFFFFF),
        Color(0x3300F2FE),
        Color(0x1AFFFFFF),
        Color(0x2BFF2A85),
        Color(0x14FFFFFF)
    )
)

val LiquidGlassSubtleBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color(0x4DFFFFFF),
        Color(0x1AFFFFFF),
        Color(0x0DFFFFFF)
    )
)

val LiquidBrandGradient = Brush.horizontalGradient(
    colors = listOf(
        LiquidCyan,
        LiquidElectricBlue,
        LiquidNeonPurple,
        LiquidNeonPink,
        LiquidAmber
    )
)

val LiquidStoryRingGradient = Brush.sweepGradient(
    colors = listOf(
        LiquidCyan,
        LiquidNeonPurple,
        LiquidNeonPink,
        LiquidAmber,
        LiquidCyan
    )
)

// --- Liquid Glass Modifier ---
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = GlassFillCard,
    borderBrush: Brush = LiquidGlassBorderBrush,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(backgroundColor, shape)
    .border(borderWidth, borderBrush, shape)

// --- Ambient Liquid Canvas Background ---
@Composable
fun LiquidAmbientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LiquidDeepSpace)
            .drawBehind {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Top right liquid magenta orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(LiquidNeonPurple.copy(alpha = 0.35f), Color.Transparent),
                        center = Offset(canvasWidth * 0.9f, canvasHeight * 0.12f),
                        radius = canvasWidth * 0.85f
                    )
                )

                // Left liquid cyan orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(LiquidCyan.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(canvasWidth * 0.08f, canvasHeight * 0.42f),
                        radius = canvasWidth * 0.75f
                    )
                )

                // Bottom right vivid pink orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(LiquidNeonPink.copy(alpha = 0.25f), Color.Transparent),
                        center = Offset(canvasWidth * 0.85f, canvasHeight * 0.78f),
                        radius = canvasWidth * 0.8f
                    )
                )

                // Center deep indigo glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(LiquidDarkIndigo.copy(alpha = 0.5f), Color.Transparent),
                        center = Offset(canvasWidth * 0.5f, canvasHeight * 0.5f),
                        radius = canvasWidth * 0.9f
                    )
                )
            }
    ) {
        content()
    }
}
