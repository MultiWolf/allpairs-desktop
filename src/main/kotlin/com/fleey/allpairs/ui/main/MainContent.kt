package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.ui.component.allpairs.Table
import com.fleey.allpairs.util.toParameters
import com.fleey.allpairs.util.validate
import com.fleey.swipebox.SwipeAction
import com.fleey.swipebox.SwipeActionsBox
import io.github.pavelicii.allpairs4j.AllPairs
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
  paramList: List<Param>,
  pagerState: PagerState,
  onUpdateParam: (Param) -> Unit,
  onRemoveParam: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()
  HorizontalPager(
    state = pagerState,
    modifier = Modifier.fillMaxSize(),
    userScrollEnabled = false
  ) { page ->
    when (page) {
      0 -> EditPage(
        paramList, onUpdateParam, onRemoveParam,
        { scope.launch { pagerState.animateScrollToPage(1) } },
        modifier
      )
      
      1 -> ResultPage(paramList, modifier.fillMaxSize())
    }
  }
}

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
          icon = rememberVectorPainter(Icons.Default.Delete),
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

@Composable
fun ResultPage(
  paramList: List<Param>,
  modifier: Modifier = Modifier
) {
  if (!paramList.validate()) {
    Text("因子不合法，请检查！")
    return
  }
  
  val headers = listOf("序号") + paramList.map { it.name }
  val allPairs = AllPairs.AllPairsBuilder().withParameters(paramList.toParameters()).build()
  val bodyData = allPairs.toBodyData()
  
  Surface(
    modifier = modifier.background(MaterialTheme.colors.surface),
    elevation = 1.dp
  ) {
    Column {
      Table(row = bodyData.size, col = headers.size, headerData = headers) { row, col ->
        when (col) {
          0 -> Text("${row + 1}", color = MaterialTheme.colors.primary)
          else -> Text(bodyData[row].values[col - 1], color = Color.Gray)
        }
      }
    }
  }
}

data class Item(val index: Int, val values: List<String>)

private fun AllPairs.toBodyData(): List<Item> {
  return this.mapIndexed { index, values ->
    Item(index, values.map { it.toString().substringAfter("=") })
  }
}