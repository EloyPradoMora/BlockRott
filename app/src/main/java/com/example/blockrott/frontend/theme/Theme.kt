package com.example.blockrott.frontend.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val BRColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = TextPrimary,
    secondary = SecondaryColor,
    onSecondary = TextPrimary,


    background = AppBackground,
    onBackground = TextPrimary,
    surface = ComponentBackground,
    onSurface = TextPrimary,

    error = Color.Red,
    onError = Color.White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = BRColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}