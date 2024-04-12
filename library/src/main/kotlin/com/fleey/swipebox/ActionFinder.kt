package com.fleey.swipebox

import kotlin.math.abs

internal data class SwipeActionMeta(
  val value: SwipeAction,
  val isOnRightSide: Boolean,
)

internal data class ActionFinder(
  val left: List<SwipeAction>,
  val right: List<SwipeAction>
) {
  
  fun actionAt(offset: Float, totalWidth: Int): SwipeActionMeta? {
    if (offset == 0f) {
      return null
    }
    
    val isOnRightSide = offset < 0f
    val actions = if (isOnRightSide) right else left
    
    val actionAtOffset = actions.actionAt(
      offset = abs(offset).coerceAtMost(totalWidth.toFloat()),
      totalWidth = totalWidth
    )
    
    return actionAtOffset?.let {
      SwipeActionMeta(
        value = actionAtOffset,
        isOnRightSide = isOnRightSide
      )
    }
  }
  
  private fun List<SwipeAction>.actionAt(offset: Float, totalWidth: Int): SwipeAction? {
    if (isEmpty()) return null
    
    val totalWeights = sumOf { it.iconSize.value.toInt() }
    
    val actionWithWidths = map { it to (it.iconSize.value / totalWeights) * totalWidth }
    val actionEndXs = actionWithWidths.scan(offsetSoFar) { acc, (action, width) -> acc + width }
    
    return zip(actionEndXs).firstOrNull { (action, actionEndX) ->
      offset <= actionEndX
    }?.first
      ?: error("Couldn't find any swipe action. Width=$totalWidth, offset=$offset, actions=$this")
  }
}