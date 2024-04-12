package com.fleey.swipebox

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.*
import kotlin.math.abs

@Composable
fun rememberSwipeActionsState(): SwipeActionsState {
  return remember { SwipeActionsState() }
}

@Stable
class SwipeActionsState internal constructor() {
  private var offsetState = mutableStateOf(0f)
  
  val offset: State<Float> get() = offsetState
  
  val isResettingOnRelease: Boolean by derivedStateOf {
    swipedAction != null
  }
  
  private var isAnimating = false
  internal var layoutWidth by mutableStateOf(0)
  internal var swipeThresholdPx by mutableStateOf(0f)
  
  internal var actions by mutableStateOf(ActionFinder(left = emptyList(), right = emptyList()))
  
  internal var swipedAction by mutableStateOf<SwipeActionMeta?>(null)
  
  internal val draggableState = DraggableState { delta ->
    val targetOffset = offsetState.value + delta
    
    val canSwipeTowardsRight = actions.left.isNotEmpty()
    val canSwipeTowardsLeft = actions.right.isNotEmpty()
    
    val isAllowed = isResettingOnRelease
        || targetOffset == 0f
        || (targetOffset > 0f && canSwipeTowardsRight)
        || (targetOffset < 0f && canSwipeTowardsLeft)
    
    val isReachLimit = hasCrossedSwipeLimit()
    offsetState.value += if ((isAllowed && !isReachLimit) || isAnimating) delta else delta / 10
  }
  
  internal fun hasCrossedSwipeLimit(): Boolean {
    return abs(offsetState.value) > (swipeThresholdPx * if (offsetState.value > 0f) actions.left.size else actions.right.size)
  }
  
  internal suspend fun handleOnDragStopped() {
    with(draggableState) {
      drag(MutatePriority.PreventUserInput) {
        val limit =
          (swipeThresholdPx * if (offsetState.value > 0f) actions.left.size else actions.right.size)
        val isReachLimit = abs(offsetState.value) > (limit * 2) / 3
        val factor = if (offsetState.value > 0) 1 else -1
        isAnimating = true
        Animatable(offsetState.value).animateTo(
          targetValue = if (isReachLimit) limit * factor else 0f,
          animationSpec = tween(
            durationMillis = if (isReachLimit) animationLimitMs else animationDurationMs,
            easing = LinearEasing
          )
        ) {
          dragBy(value - offsetState.value)
        }
        isAnimating = false
      }
      swipedAction = null
    }
  }
  
  internal suspend fun handleReset() {
    with(draggableState) {
      drag(MutatePriority.PreventUserInput) {
        isAnimating = true
        Animatable(offsetState.value).animateTo(
          targetValue = 0f,
          animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = LinearEasing
          )
        ) {
          dragBy(value - offsetState.value)
        }
        isAnimating = false
      }
    }
  }
}