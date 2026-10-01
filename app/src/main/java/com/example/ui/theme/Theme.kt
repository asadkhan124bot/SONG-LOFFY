package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MissionDarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = ScreenBackgroundDark,
    secondary = AmberTierText,
    onSecondary = ScreenBackgroundDark,
    tertiary = MissionRewardGreen,
    onTertiary = ScreenBackgroundDark,
    background = ScreenBackgroundDark,
    onBackground = TextPrimaryWhite,
    surface = CardBackgroundDark,
    onSurface = TextPrimaryWhite,
    surfaceVariant = SubBoxBackgroundDark,
    onSurfaceVariant = TextSecondaryGrey,
    outline = White12Border
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = MissionDarkColorScheme,
        typography = Typography,
        content = content
    )
}
