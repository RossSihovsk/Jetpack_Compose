package com.ross.instagramui.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColors(
    primary = Purple200,
    background = Color.Black,
    primaryVariant = Purple700,
    secondary = Color.DarkGray,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    background = Color.White,
    primaryVariant = Purple700,
    secondary = Color.LightGray,
    onBackground = Color.Black
)
@Composable
fun InstagramUITheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val view = LocalView.current

    val color = MaterialTheme.colors.background.toArgb()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = color
            window.navigationBarColor = color

            WindowCompat.getInsetsController(window, view)
                ?.isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view)
                ?.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}