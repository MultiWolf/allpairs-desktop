package com.fleey.allpairs.ui.main.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fleey.allpairs.data.entity.AllPairsItem
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.data.state.ExportState
import com.fleey.allpairs.extender.openFileInEnv
import com.fleey.allpairs.extender.toParameters
import com.fleey.allpairs.extender.validate
import com.fleey.allpairs.handler.ExportResultHandler
import com.fleey.allpairs.handler.ExportType
import com.fleey.allpairs.ui.component.*
import io.github.pavelicii.allpairs4j.AllPairs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResultPage(
  isDark: Boolean,
  paramList: List<Param>,
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
  
  var exportTypeState by remember { mutableStateOf(ExportState()) }
  
  val successDialog = NotificationDialog(
    DialogType.SUCCESS,
    title = "是否打开文件",
    positiveText = "打开",
    subTitle = "文件已保存至\n${exportTypeState.filePath}"
  ) {
    exportTypeState.filePath.openFileInEnv()
    it.hide()
  }
  
  val errorDialog = NotificationDialog(
    DialogType.ERROR,
    title = "保存失败",
    subTitle = exportTypeState.errorMessage,
  )
  
  val sheetContent: @Composable ColumnScope.() -> Unit = {
    Column {
      ListItem(text = { Text("选择你要导出的方式~") })
      ExportTypeListItem(ExportType.entries) { exportTypeState = ExportState(exportType = it) }
    }
  }
  
  CustomBottomSheet(
    state = bottomSheetState,
    sheetContent = sheetContent
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      Surface(
        modifier = Modifier
          .fillMaxHeight()
          .weight(1f)
          .background(MaterialTheme.colors.surface),
        elevation = 1.dp
      ) {
        ResultTable(headers, bodyData)
      }
      Button(
        onClick = { scope.launch { bottomSheetState.show() } },
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
      ) {
        Text("导出")
      }
    }
  }
  
  if (exportTypeState.exportType != ExportType.NULL) {
    val tempExportState = exportTypeState
    exportTypeState = ExportState()
    handleExportResult(
      tempExportState,
      headers,
      bodyData,
      isDark,
      onSuccess = {
        scope.launch {
          bottomSheetState.hide()
          exportTypeState.filePath = it
          successDialog.show()
        }
      },
      onFailure = {
        exportTypeState.errorMessage = it.message.toString()
        errorDialog.show()
      }
    )
  }
}

private fun AllPairs.toBodyData(): List<AllPairsItem> {
  return this.mapIndexed { index, values ->
    AllPairsItem(index, values.map { it.toString().substringAfter("=") })
  }
}

@Composable
fun handleExportResult(
  exportState: ExportState,
  headers: List<String>,
  bodyData: List<AllPairsItem>,
  isDark: Boolean,
  onSuccess: (String) -> Unit,
  onFailure: (Exception) -> Unit,
) {
  ExportResultHandler.exportResultAsOrderType(
    exportState.exportType,
    headers,
    bodyData,
    isDark,
    onSuccess = { filePath -> filePath?.let { onSuccess(it) } },
    onFailure = { error -> onFailure(error) }
  )
}
