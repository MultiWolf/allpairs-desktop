package com.fleey.swipebox

import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SwipeActionsBox(
  modifier: Modifier = Modifier,
  state: SwipeActionsState = rememberSwipeActionsState(),
  startActions: List<SwipeAction> = emptyList(),
  endActions: List<SwipeAction> = emptyList(),
  swipeThreshold: Dp = 40.dp,
  content: @Composable BoxScope.() -> Unit
) {
  BoxWithConstraints(modifier) {
    state.layoutWidth = constraints.maxWidth
    state.swipeThresholdPx = with(LocalDensity.current) { swipeThreshold.toPx() }
    
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    state.actions = remember(endActions, startActions, isRtl) {
      ActionFinder(
        left = if (isRtl) endActions else startActions,
        right = if (isRtl) startActions else endActions
      )
    }
    
    val scope = rememberCoroutineScope()
    val offsetX by state.offset
    
    Box(
      modifier = Modifier
        .offset { IntOffset(x = offsetX.roundToInt(), y = 0) }
        .draggable(
          orientation = Horizontal,
          enabled = !state.isResettingOnRelease,
          onDragStopped = {
            produce<Unit> {
              state.handleOnDragStopped()
            }
          },
          state = state.draggableState,
        ),
      content = content
    )
    
    val actionWidthDp = with(LocalDensity.current) { abs(offsetX).toDp() }
    
    state.actions.right.forEachIndexed { index, action ->
      ActionIconBox(
        action = action,
        actionWidth = actionWidthDp / state.actions.right.size.toFloat(),
        swipeThreshold = swipeThreshold,
        onClick = {
          handleActionClick(action, state, scope)
        }
      )
    }
    
    state.actions.left.forEachIndexed { index, action ->
      ActionIconBox(
        action = action,
        actionWidth = actionWidthDp / state.actions.left.size.toFloat(),
        swipeThreshold = swipeThreshold,
        onClick = {
          handleActionClick(action, state, scope)
        }
      )
    }
  }
}

private fun handleActionClick(
  action: SwipeAction,
  state: SwipeActionsState,
  scope: CoroutineScope
) {
  if (action.resetAfterClick) {
    scope.launch {
      state.handleReset()
      action.onClick()
    }
  } else {
    action.onClick()
  }
}