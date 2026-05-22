package com.lucas.calculadorapenal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    secondary = GoldDark,

    background = BackgroundDark,
    surface = CardBlue,

    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun CalculadoraPenalTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}