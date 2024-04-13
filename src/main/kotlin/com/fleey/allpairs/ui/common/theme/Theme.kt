package com.fleey.allpairs.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.fleey.allpairs.ui.common.theme.color.dark.DarkColorPalette
import com.fleey.allpairs.ui.common.theme.color.light.LightColorPalette

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

val themes = mapOf(
  false to LightColorPalette,
  true to DarkColorPalette
)

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
