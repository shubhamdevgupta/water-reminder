package com.shubhamdev.waterreminder.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAEF5),
    secondary = Color(0xFF89D5FF),
    tertiary = Color(0xFF005BBB)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAEF5),
    onPrimary = Color.White,
    secondary = Color(0xFF89D5FF),
    tertiary = Color(0xFF005BBB),
    background = Color(0xFFF7FAFF),
    surface = Color.White,
    onSurface = Color(0xFF1C1C1C),
    onSurfaceVariant = Color(0xFF666666),
    outline = Color(0xFFE5E5E5)
)

@Composable
fun WaterReminderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(28.dp)
        ),
        content = content
    )
}
