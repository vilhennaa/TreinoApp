package com.gustavofelipe.treino.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkNeonColorScheme = darkColorScheme(
    primary = NeonOrange,
    onPrimary = Color.White,
    primaryContainer = NeonOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = NeonOrangeLight,
    secondaryContainer = NeonOrangeContainer,
    onSecondaryContainer = OnNeonOrangeContainer,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color.LightGray
)

@Composable
fun TreinoTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkNeonColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}