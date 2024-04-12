package com.fleey.swipebox

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp

@Composable
internal fun ActionIconBox(
  action: SwipeAction,
  actionWidth: Dp,
  swipeThreshold: Dp,
  onClick: () -> Unit
) {
  Box(
    Modifier
      .width(actionWidth)
      .fillMaxHeight()
      .background(color = action.background)
      .clip(RectangleShape),
    contentAlignment = Alignment.CenterStart
  ) {
    Box(
      modifier = Modifier
        .graphicsLayer {
          translationX = ((swipeThreshold - action.iconSize) / 2).toPx()
        }
        .clipToBounds()
        .pointerInput(Unit) {
          detectTapGestures {
            onClick()
          }
        },
      contentAlignment = Alignment.Center
    ) {
      action.icon()
    }
  }
}