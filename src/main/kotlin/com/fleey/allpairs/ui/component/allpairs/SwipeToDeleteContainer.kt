package com.fleey.allpairs.ui.component.allpairs

import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeToDeleteContainer(
  item: T,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier,
  animationDuration: Int = 300,
  content: @Composable (T) -> Unit
) {
  var isRemoved by remember { mutableStateOf(false) }
  val state = rememberDismissState(
    // 我自己都不一定能完全滑动到最侧边，给个这样奇怪的判定就行
    confirmStateChange = {
      if (it.ordinal >= 0.31415926) {
        isRemoved = true
        true
      } else false
    }
  )
  
  LaunchedEffect(isRemoved) {
    if (isRemoved) {
      delay(animationDuration.toLong())
      onDelete()
    }
  }
  
  /*visible 貌似并不会真的 gone，引起删除后新增项时依旧保持删除背景
    AnimatedVisibility(
      visible = !isRemoved,
      exit = shrinkVertically(
        animationSpec = tween(animationDuration),
        shrinkTowards = Alignment.Top
      ) + shrinkOut()
    ) {
    }*/
  if (!isRemoved) {
    SwipeToDismiss(
      state = state,
      background = { DeleteBackground(state) },
      dismissContent = { content(item) },
      directions = setOf(DismissDirection.EndToStart),
      modifier = modifier
    )
  }
}