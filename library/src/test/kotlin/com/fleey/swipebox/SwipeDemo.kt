package com.fleey.swipebox

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

internal data class DemoData(
  val id: Int,
  val title: String,
)

@Composable
@Preview
internal fun SwipeDemo() {
  val data = remember { mutableStateListOf<DemoData>() }
  
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
  ) {
    // items 务必添加 key，否则会造成显示错乱
    itemsIndexed(data, key = { index, item -> item.id }) { index, item ->
      // index 和 item 都是最原始的数据，一旦 onDelete 和 onChange 过，index 和 item 就都不准了，因此根据 item 的 id 作为唯一标识查找
      val delete = SwipeAction(
        icon = rememberVectorPainter(Icons.Default.Delete),
        background = Color.Red,
        onClick = { data.remove(data.find { it.id == item.id }) },
      )
      val change = SwipeAction(
        icon = { Text("add") },
        background = Color.Green,
        onClick = {
          data[data.indexOf(data.find { it.id == item.id })] =
            item.copy(title = "Item has change: ${item.id}")
        },
        resetAfterClick = true,
        iconSize = 20.dp
      )
      // paramList.subList(it, paramList.size).forEachIndexed { i, param ->
      //      paramList[it + i] =
      //        param.copy(id = it + i, name = "因子${it + i + 1}")
      //    }
      val change2 = SwipeAction(
        icon = rememberVectorPainter(Icons.Default.Add),
        background = Color.Blue,
        onClick = {
          data[data.indexOf(data.find { it.id == item.id })] =
            item.copy(title = "Item has change: ${item.id}")
        },
      )
      SwipeActionsBox(
        leftActions = listOf(change),
        rightActions = listOf(delete, change2),
        swipeThreshold = 80.dp
      ) {
        Box(
          Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White),
          contentAlignment = Alignment.Center,
        ) {
          Text(item.title)
        }
      }
    }
  }
}