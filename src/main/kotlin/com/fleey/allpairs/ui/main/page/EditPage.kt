package com.fleey.allpairs.ui.main.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddLocation
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.ui.main.ParamItem
import com.fleey.swipebox.SwipeAction
import com.fleey.swipebox.SwipeActionsBox
import kotlinx.coroutines.delay

@Composable
fun EditPage(
  paramList: List<Param>,
  onUpdateParam: (Param) -> Unit,
  onRemoveParam: (Int) -> Unit,
  onAddParamAfter: (Int) -> Unit,
  onGenerateClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .then(modifier)
  ) {
    LazyColumn(
      Modifier.fillMaxHeight().weight(1f),
      state = rememberLazyListState()
    ) {
      items(paramList, key = { item -> item.random }) { item ->
        var isRemoved by remember { mutableStateOf(false) }
        val animationDuration = 360
        
        val addItemAction = SwipeAction(
          icon = rememberVectorPainter(Icons.Rounded.AddLocation),
          background = Color.Green,
          onClick = { onAddParamAfter(item.id) }
        )
        
        val deleteItemAction = SwipeAction(
          icon = rememberVectorPainter(Icons.Rounded.Delete),
          background = Color.Red,
          onClick = { isRemoved = true }
        )
        
        LaunchedEffect(isRemoved) {
          if (isRemoved) {
            delay(animationDuration.toLong())
            onRemoveParam(item.id)
          }
        }
        
        AnimatedVisibility(
          visible = !isRemoved,
          exit = shrinkVertically(
            animationSpec = tween(animationDuration),
            shrinkTowards = Alignment.Bottom
          ) + fadeOut(tween(animationDuration / 3)),
          modifier = Modifier.padding(bottom = if (isRemoved) 0.dp else 8.dp)
        ) {
          SwipeActionsBox(
            rightActions = listOf(addItemAction, deleteItemAction)
          ) {
            ParamItem(
              param = item,
              onUpdate = { onUpdateParam(it) },
            )
          }
        }
        
      }
    }
    Button(
      onClick = onGenerateClick,
      modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
      Text("生成")
    }
  }
}