package com.fleey.allpairs.ui.main.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.ui.main.ParamItem
import com.fleey.swipebox.SwipeAction
import com.fleey.swipebox.SwipeActionsBox

@Composable
fun EditPage(
  paramList: List<Param>,
  onUpdateParam: (Param) -> Unit,
  onRemoveParam: (Int) -> Unit,
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
      verticalArrangement = Arrangement.spacedBy(8.dp),
      state = rememberLazyListState()
    ) {
      items(paramList, key = { item -> item.random }) { item ->
        val deleteItemAction = SwipeAction(
          icon = rememberVectorPainter(Icons.Rounded.Delete),
          background = Color.Red,
          onClick = { onRemoveParam(item.id) }
        )
        SwipeActionsBox(rightActions = listOf(deleteItemAction)) {
          ParamItem(
            param = item,
            onUpdate = { onUpdateParam(it) },
          )
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