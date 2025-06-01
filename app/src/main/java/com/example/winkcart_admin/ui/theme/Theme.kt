package com.example.winkcart_admin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/*
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    */
/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    *//*

)
*/
private val DarkColorScheme = darkColorScheme(
    primary = Rose80,
    onPrimary = Rose10,
    primaryContainer = Rose20,
    onPrimaryContainer = Color.White,

    secondary = Rose60,
    onSecondary = Rose10,
    secondaryContainer = Rose20,
    onSecondaryContainer = Color.White,

    tertiary = Rose40,
    onTertiary = Color.White
)

//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80


private val LightColorScheme = lightColorScheme(
    primary = Rose40,
    onPrimary = Color.White,
    primaryContainer = Rose80,
    onPrimaryContainer = Rose10,

    secondary = Rose20,
    onSecondary = Color.White,
    secondaryContainer = Rose80,
    onSecondaryContainer = Rose10,

    tertiary = Rose60,
    onTertiary = Color.White
)
@Composable
fun WinkCart_AdminTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}