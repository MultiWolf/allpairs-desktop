package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fleey.allpairs.data.entity.AllPairsItem
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.extender.toParameters
import com.fleey.allpairs.extender.validate
import com.fleey.allpairs.handler.ExportResultHandler
import com.fleey.allpairs.handler.ExportType
import com.fleey.allpairs.ui.component.CustomBottomSheet
import com.fleey.allpairs.ui.component.Table
import com.fleey.allpairs.util.IconUtil.fromResToIcon
import com.fleey.swipebox.SwipeAction
import com.fleey.swipebox.SwipeActionsBox
import io.github.pavelicii.allpairs4j.AllPairs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainContent(
  paramList: List<Param>,
  pagerState: PagerState,
  onUpdateParam: (Param) -> Unit,
  onRemoveParam: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val resultBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
  val scope = rememberCoroutineScope()
  
  HorizontalPager(
    state = pagerState,
    modifier = Modifier.fillMaxSize(),
    userScrollEnabled = false
  ) { page ->
    when (page) {
      0 -> {
        EditPage(
          paramList, onUpdateParam, onRemoveParam,
          { scope.launch { pagerState.animateScrollToPage(1) } },
          modifier
        )
        scope.launch { resultBottomSheetState.hide() }
      }
      
      1 -> ResultPage(paramList, modifier.fillMaxSize(), scope, resultBottomSheetState)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResultPage(
  paramList: List<Param>,
  modifier: Modifier = Modifier,
  scope: CoroutineScope,
  bottomSheetState: ModalBottomSheetState
) {
  if (!paramList.validate()) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "因子不合法，请检查",
        color = MaterialTheme.colors.error,
        fontSize = 20.sp
      )
    }
    return
  }
  
  val headers = listOf("序号") + paramList.map { it.name }
  val allPairs = AllPairs.AllPairsBuilder().withParameters(paramList.toParameters()).build()
  val bodyData = allPairs.toBodyData()
  
  val sheetContent: @Composable ColumnScope.() -> Unit = {
    Column {
      ListItem(text = { Text("选择你要导出的方式~") })
      
      ExportTypeListItem(headers, bodyData, scope, bottomSheetState)
    }
  }
  
  CustomBottomSheet(bottomSheetState, { sheetContent() }) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      Surface(
        modifier = modifier
          .fillMaxHeight()
          .weight(1f)
          .background(MaterialTheme.colors.surface),
        elevation = 1.dp
      ) {
        Table(
          row = bodyData.size,
          col = headers.size,
          headerData = headers,
        ) { row, col ->
          when (col) {
            0 -> Text("${row + 1}", color = MaterialTheme.colors.primary)
            else -> Text(bodyData[row].values[col - 1], color = Color.Gray)
          }
        }
      }
      Button(
        onClick = { scope.launch { bottomSheetState.show() } },
        modifier = Modifier.fillMaxWidth().padding(8.dp)
      ) {
        Text("导出")
      }
    }
  }
}

private fun AllPairs.toBodyData(): List<AllPairsItem> {
  return this.mapIndexed { index, values ->
    AllPairsItem(index, values.map { it.toString().substringAfter("=") })
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExportTypeListItem(
  headers: List<String>,
  bodyData: List<AllPairsItem>,
  scope: CoroutineScope,
  bottomSheetState: ModalBottomSheetState,
) {
  var chosenExportType by remember { mutableStateOf(ExportType.NULL) }
  
  ExportType.entries.forEach { exportType ->
    if (exportType == ExportType.NULL) return@forEach
    val exportTypeName = exportType.name.lowercase()
    val text = when (exportType) {
      ExportType.TEXT -> "复制至剪贴板"
      else -> "导出为 $exportTypeName"
    }
    
    ListItem(
      icon = { "ic_export_$exportTypeName.svg".fromResToIcon() },
      text = { Text(text) },
      modifier = Modifier.clickable {
        chosenExportType = exportType
      }
    )
  }
  
  if (chosenExportType != ExportType.NULL) {
    ExportResultHandler.exportResultAsOrderType(
      chosenExportType,
      headers,
      bodyData,
      onSuccess = {
        scope.launch {
          bottomSheetState.hide()
        }
      }
    )
    chosenExportType = ExportType.NULL
  }
  
}