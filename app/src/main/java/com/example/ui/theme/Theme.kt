package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LiquidGlassColorScheme = darkColorScheme(
    primary = LiquidCyan,
    onPrimary = Color(0xFF070A14),
    secondary = LiquidNeonPink,
    onSecondary = Color.White,
    tertiary = LiquidNeonPurple,
    background = LiquidDeepSpace,
    onBackground = Color.White,
    surface = Color.Transparent, // Allows underlying ambient liquid gradients to shine through glass
    onSurface = Color.White,
    surfaceVariant = Color(0x24FFFFFF),
    onSurfaceVariant = Color(0xFFB5B2CE),
    outline = Color(0x40FFFFFF)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LiquidGlassColorScheme,
        typography = Typography,
        content = content
    )
}
