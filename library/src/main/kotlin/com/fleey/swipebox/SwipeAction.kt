package com.fleey.swipebox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class SwipeAction(
  val onClick: () -> Unit,
  val icon: @Composable () -> Unit,
  val background: Color,
  val iconSize: Dp,
  val resetAfterClick: Boolean
)

fun swipeAction(
  onClick: () -> Unit,
  icon: Painter,
  iconSize: Dp = 24.dp,
  background: Color,
  resetAfterClick: Boolean = true
) = SwipeAction(
  onClick = onClick,
  icon = {
    Image(
      modifier = Modifier.size(iconSize),
      painter = icon,
      contentDescription = null
    )
  },
  background = background,
  iconSize = iconSize,
  resetAfterClick = resetAfterClick
)