package com.fleey.allpairs.ui.common.theme.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import kotlin.properties.Delegates

@Stable
class AppColors(
  background: Color,
  onBackground: Color,
  secondary: Color,
  primary: Color,
  surface: Color,
  onSurface: Color,
  onPrimary: Color,
  onSecondary: Color
) {
  private var propertyDelegate: Color by Delegates.notNull()
  
  var background by ::propertyDelegate
  var onBackground by ::propertyDelegate
  var secondary by ::propertyDelegate
  var primary by ::propertyDelegate
  var surface by ::propertyDelegate
  var onSurface by ::propertyDelegate
  var onPrimary by ::propertyDelegate
  var onSecondary by ::propertyDelegate
}