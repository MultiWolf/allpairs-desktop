package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.ui.component.allpairs.SwipeToDeleteContainer
import com.fleey.allpairs.ui.component.allpairs.Table
import com.fleey.allpairs.util.toParameters
import com.fleey.allpairs.util.validate
import io.github.pavelicii.allpairs4j.AllPairs
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
  paramList: List<Param>,
  pagerState: PagerState,
  onUpdateParam: (Int, Param) -> Unit,
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
      0 -> {
        EditPage(
          paramList = paramList,
          onUpdateParam = onUpdateParam,
          onRemoveParam = onRemoveParam,
          onGenerateClick = { scope.launch { pagerState.animateScrollToPage(1) } },
          modifier = Modifier.fillMaxSize().then(modifier)
        )
      }
      
      1 -> {
        ResultPage(
          paramList = paramList,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

@Composable
fun EditPage(
  paramList: List<Param>,
  onUpdateParam: (Int, Param) -> Unit,
  onRemoveParam: (Int) -> Unit,
  onGenerateClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .then(modifier)
  ) {
    Column(
      Modifier.fillMaxHeight().weight(1f).verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      paramList.forEachIndexed { index, param ->
        SwipeToDeleteContainer(
          item = param,
          onDelete = { onRemoveParam(index) },
          content = {
            ParamItem(
              param = param,
              onUpdate = { onUpdateParam(index, it) },
              paramNumber = index + 1,
              modifier = Modifier.fillMaxWidth()
            )
          },
        )
      }
    }
    Button(
      onClick = onGenerateClick,
      modifier = Modifier.fillMaxWidth()
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
  
  Column(modifier = modifier) {
    Table(row = bodyData.size, col = headers.size, headerData = headers) { row, col ->
      when (col) {
        0 -> Text("${row + 1}", color = MaterialTheme.colors.primary)
        else -> Text(bodyData[row].values[col - 1], color = Color.Gray)
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