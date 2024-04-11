package com.fleey.allpairs.ui.component.allpairs

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteBackground(
  swipeDismissState: DismissState
) {
  val color by animateColorAsState(
    when (swipeDismissState.dismissDirection) {
      DismissDirection.EndToStart -> Color.Red
      DismissDirection.StartToEnd -> Color.Transparent
      else -> Color.Transparent
    }
  )
  
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color)
      .padding(16.dp),
    contentAlignment = Alignment.CenterEnd
  ) {
    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete icon", tint = Color.White)
  }
}
