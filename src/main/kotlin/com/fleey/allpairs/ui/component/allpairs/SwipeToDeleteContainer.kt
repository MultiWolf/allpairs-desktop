package com.fleey.allpairs.ui.component.allpairs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeToDeleteContainer(
  item: T,
  onDelete: (T) -> Unit,
  modifier: Modifier = Modifier,
  animationDuration: Int = 500,
  content: @Composable (T) -> Unit
) {
  var isRemoved by remember {
    mutableStateOf(false)
  }
  val state = rememberDismissState(
    confirmStateChange = { dismissValue ->
      if (dismissValue == DismissValue.DismissedToStart) {
        isRemoved = true
        true
      } else false
      // 包含滑动的方向信息，松手时触发执行
    }
    //positionalThreshold = {
    //      // 滑动到什么位置会改变状态
    //      it / 2
    //}
  )
  
  LaunchedEffect(key1 = isRemoved) {
    if (isRemoved) {
      delay(animationDuration.toLong())
      onDelete(item)
    }
  }
  
  AnimatedVisibility(
    visible = !isRemoved,
    exit = shrinkVertically(
      animationSpec = tween(durationMillis = animationDuration),
      shrinkTowards = Alignment.Top
    ) + fadeOut()
  ) {
    SwipeToDismiss(
      state = state,
      background = { DeleteBackground(swipeDismissState = state) },
      dismissContent = { content(item) },
      directions = setOf(DismissDirection.EndToStart),
      modifier = modifier
    )
  }
}