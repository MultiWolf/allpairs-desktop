package com.fleey.swiptodelete

import androidx.compose.material.*
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
    confirmStateChange = {
      if (it == DismissValue.DismissedToStart) {
        isRemoved = true
        true
      } else false
    }
  )
  
  LaunchedEffect(isRemoved) {
    if (isRemoved) {
      delay(animationDuration.toLong())
      onDelete()
      isRemoved = false
    }
  }
  
  if (!isRemoved) {
    SwipeToDismiss(
      state = state,
      background = { DeleteBackground(state) },
      dismissContent = { content(item) },
      directions = setOf(DismissDirection.EndToStart),
      modifier = modifier,
      dismissThresholds = { FractionalThreshold(0.2f) }
    )
  }
}