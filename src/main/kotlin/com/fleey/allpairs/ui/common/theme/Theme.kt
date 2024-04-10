package com.fleey.allpairs.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/*
val AppColorsProvider = compositionLocalOf { lightColorPalette }

@Composable
fun AppTheme(
  isDark: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors: AppColors = remember { if (isDark) darkColorPalette else lightColorPalette }
  
  CompositionLocalProvider(AppColorsProvider provides colors) {
    MaterialTheme(
      shapes = shapes,
    ) {
      content()
    }
  }
}*/

@Composable
fun AppTheme(
  isDark: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = if (isDark) DarkColorPalette else LightColorPalette
  
  MaterialTheme(
    colors = colors,
    content = content
  )
}

private val LightColorPalette = lightColors(
  background = Color(0xFFF2F3F5),
  onBackground = Color(0xFFABB1B8),
  secondary = Color(0xFF5765F2),
  primary = Color(0xFF5765F2),
  surface = Color(0xFFFFFFFF),
  onSurface = Color(0xFF4F5660),
  onPrimary = Color(0xFFFFFFFF),
  onSecondary = Color(0xFFE3E5E8),
)

private val DarkColorPalette = darkColors(
  background = Color(0xFF1F2225),
  onBackground = Color(0xFFABB1B8),
  secondary = Color(0xFF5765F2),
  primary = Color(0xFF5765F2),
  surface = Color(0xFF1F2225),
  onSurface = Color(0xFFABB1B8),
  onPrimary = Color(0xFFFFFFFF),
  onSecondary = Color(0xFF40444B),
)
