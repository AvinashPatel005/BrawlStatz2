package com.kal.brawlstatz2.ui.theme

import android.app.Activity
import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)
private val LightColors = lightColorScheme(
    onPrimary = Black1,
    primary = PurpleGrey80,
    background = Purple80,
    onSecondary = Black2,
)
private val DarkColors = lightColorScheme(
    background= Black,
    primary = Black1,
    secondary = Black2,
    onPrimary = White1,
    onSecondary = White2,
    primaryContainer = Black2,
    secondaryContainer = Black3,
    tertiaryContainer = PurpleGrey40,
    onTertiaryContainer = PurpleGrey80,
    onBackground = White1
)
@Composable
fun AppTheme(
    themeMode: Int,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val context = LocalContext.current
    val colorScheme = if(themeMode==0) DarkColors else if(themeMode==1) LightColors else {
        if(isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
    ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = if(themeMode==0||themeMode==1) false else !isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ){
        content()
    }
}

//@Composable
//fun BrawlStatz2Theme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = false,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
//            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
//        }
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}