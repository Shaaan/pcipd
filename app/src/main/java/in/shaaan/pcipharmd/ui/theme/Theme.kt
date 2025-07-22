// app/src/main/java/in/shaaan/pcipharmd/ui/theme/Theme.kt

package `in`.shaaan.pcipharmd.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// What: The color scheme for dark mode.
// Why: Provides a specific set of colors to be used when the device is in dark theme.
// How: Uses the base colors defined in Color.kt.
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary
)

// What: The color scheme for light mode.
// Why: Provides a specific set of colors for light theme.
// How: Uses the base colors defined in Color.kt.
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // What: A flag for enabling dynamic color (Material You).
    // Why: On Android 12+, the app's theme can be derived from the user's wallpaper.
    // How: Defaults to true on supported OS versions.
    dynamicColor: Boolean = true,
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

    // What: A SideEffect to change the system status bar color.
    // Why: To make the status bar match the app's theme for a more immersive experience.
    // How: It gets the current window and sets the status bar color and icon appearance.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // What: The core MaterialTheme composable.
    // Why: It applies the chosen color scheme and typography to all descendant composables.
    // How: It wraps the app's content.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}