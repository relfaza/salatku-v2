package com.example.shalatku.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Green40,
    primaryContainer = GreenContainerLight,
    secondary = Sand40,
)

private val DarkColors = darkColorScheme(
    primary = Green80,
    primaryContainer = GreenContainerDark,
    secondary = Sand80,
)

@Composable
fun ShalatkuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off on purpose: on Android 12+ dynamic colour would replace the SalatKu green with wallpaper colours.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
