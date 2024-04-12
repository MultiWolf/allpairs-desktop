package com.fleey.swipebox

import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SwipeActionsBox(
  modifier: Modifier = Modifier,
  state: SwipeActionsState = rememberSwipeActionsState(),
  leftActions: List<SwipeAction> = emptyList(),
  rightActions: List<SwipeAction> = emptyList(),
  swipeThreshold: Dp = 72.dp,
  content: @Composable BoxScope.() -> Unit
) {
  BoxWithConstraints(modifier) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    state.apply {
      layoutWidth = constraints.maxWidth
      swipeThresholdPx = LocalDensity.current.run { swipeThreshold.toPx() }
      actions = remember(leftActions, rightActions, isRtl) {
        ActionFinder(
          left = if (isRtl) rightActions else leftActions,
          right = if (isRtl) leftActions else rightActions,
        )
      }
    }
    
    val scope = rememberCoroutineScope()
    val offsetX = state.offset.value.roundToInt()
    
    Box(
      modifier = Modifier
        .absoluteOffset { IntOffset(x = offsetX, y = 0) }
        .draggable(
          orientation = Horizontal,
          enabled = !state.isResettingOnRelease,
          onDragStopped = {
            scope.launch {
              state.handleOnDragStopped()
            }
          },
          state = state.draggableState,
        ),
      content = content
    )
    
    val actionWidth = with(LocalDensity.current) { abs(offsetX).toDp() }
    val actionRowModifier = Modifier.matchParentSize()
    
    val rightActionsCondition = state.actions.right.isNotEmpty() && offsetX < 0
    val leftActionsCondition = state.actions.left.isNotEmpty() && offsetX > 0
    
    if (rightActionsCondition || leftActionsCondition) {
      val actions = if (rightActionsCondition) state.actions.right else state.actions.left
      val offset =
        if (rightActionsCondition) constraints.maxWidth + offsetX else -constraints.maxWidth + offsetX
      
      SwipeActionsRow(
        actions,
        offset,
        actionWidth,
        swipeThreshold,
        actionRowModifier
      ) { handleActionClick(it, scope, state) }
    }
  }
}

@Composable
private fun SwipeActionsRow(
  actions: List<SwipeAction>,
  offsetX: Int,
  actionWidth: Dp,
  swipeThreshold: Dp,
  modifier: Modifier,
  onClick: (SwipeAction) -> Unit
) {
  val actionWidthPerItem = actionWidth / actions.size.toFloat()
  val arrangement = if (offsetX > 0) Arrangement.Start else Arrangement.End
  
  Row(
    Modifier.absoluteOffset { IntOffset(x = offsetX, y = 0) }.then(modifier),
    horizontalArrangement = arrangement
  ) {
    actions.forEach { action ->
      ActionIconBox(
        action = action,
        actionWidth = actionWidthPerItem,
        swipeThreshold = swipeThreshold
      ) {
        onClick(action)
      }
    }
  }
}

private fun handleActionClick(
  action: SwipeAction,
  scope: CoroutineScope,
  state: SwipeActionsState
) {
  if (action.resetAfterClick) {
    scope.launch {
      state.handleReset()
      action.onClick()
    }
  }
}