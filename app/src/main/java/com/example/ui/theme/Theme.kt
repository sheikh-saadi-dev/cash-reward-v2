package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EarnRewardDarkColorScheme = darkColorScheme(
    primary = PrimaryViolet,
    onPrimary = Color.White,
    primaryContainer = VioletPrimaryDark,
    onPrimaryContainer = VioletPrimaryLight,
    secondary = TealHighlight,
    onSecondary = Color.Black,
    secondaryContainer = TealDark,
    tertiary = GoldAccent,
    onTertiary = Color.Black,
    tertiaryContainer = GoldDark,
    background = DeepVioletBackground,
    onBackground = TextWhitePrimary,
    surface = DeepVioletSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = VioletCardBackground,
    onSurfaceVariant = TextWhiteSecondary,
    outline = VioletBorder,
    error = ErrorRed
)

@Composable
fun EarnRewardTheme(
    darkTheme: Boolean = true, // Default to rich dark gradient aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EarnRewardDarkColorScheme,
        typography = Typography,
        content = content
    )
}

