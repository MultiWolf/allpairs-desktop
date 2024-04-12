package com.fleey.swipebox

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.unit.Dp

@Stable
data class SwipeAction(
  val onClick: () -> Unit,
  val icon: VectorPainter,
  val background: Color,
  val iconSize: Dp,
  val resetAfterClick: Boolean
)