package com.fleey.allpairs.ui.main.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fleey.allpairs.data.entity.AllPairsItem
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.extender.fromResToIcon
import com.fleey.allpairs.extender.openFileInEnv
import com.fleey.allpairs.extender.toParameters
import com.fleey.allpairs.extender.validate
import com.fleey.allpairs.handler.ExportResultHandler
import com.fleey.allpairs.handler.ExportType
import com.fleey.allpairs.ui.component.CustomBottomSheet
import com.fleey.allpairs.ui.component.DialogType
import com.fleey.allpairs.ui.component.NotificationDialog
import com.fleey.allpairs.ui.component.ResultTable
import io.github.pavelicii.allpairs4j.AllPairs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResultPage(
  isDark: Boolean,
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
      
      ExportTypeListItem(headers, bodyData, isDark, scope, bottomSheetState)
    }
  }
  
  CustomBottomSheet(bottomSheetState, { sheetContent() }) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      Surface(
        modifier = modifier.fillMaxHeight().weight(1f).background(MaterialTheme.colors.surface),
        elevation = 1.dp
      ) {
        ResultTable(headers, bodyData)
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
  isDark: Boolean,
  scope: CoroutineScope,
  bottomSheetState: ModalBottomSheetState,
) {
  var chosenExportType by remember { mutableStateOf(ExportType.NULL) }
  var filePath by remember { mutableStateOf("") }
  var exceptionMsg by remember { mutableStateOf("") }
  
  val successDialog = NotificationDialog(
    DialogType.SUCCESS,
    title = "是否打开文件",
    positiveText = "打开",
    subTitle = "文件已保存至\n$filePath"
  ) {
    filePath.openFileInEnv()
    it.hide()
  }
  
  val errorDialog = NotificationDialog(
    DialogType.ERROR,
    title = "保存失败",
    subTitle = exceptionMsg,
  )
  
  
  ExportType.entries.forEach { exportType ->
    if (exportType == ExportType.NULL) return@forEach
    val exportTypeName = exportType.name.lowercase()
    
    val text = when (exportType) {
      ExportType.EXCEL -> "表格"
      ExportType.HTML -> "网页"
      ExportType.IMAGE -> "图片"
      ExportType.TEXT -> "纯文字"
      ExportType.MARKDOWN -> "Markdown"
      else -> ""
    }
    val secondaryText = when (exportType) {
      ExportType.EXCEL -> "以 .xlsx 类型保存"
      ExportType.IMAGE -> "以 .png 类型保存"
      ExportType.HTML -> "以 .html 网页呈现"
      ExportType.TEXT -> "复制至剪贴板，可粘贴至 excel"
      ExportType.MARKDOWN -> "复制至剪贴板，可粘贴至 md 编辑器"
      else -> null
    }
    if (secondaryText != null) {
      ListItem(
        icon = { "ic_export_$exportTypeName.svg".fromResToIcon() },
        text = { Text(text) },
        secondaryText = { Text(secondaryText) },
        modifier = Modifier.clickable {
          chosenExportType = exportType
        }
      )
    } else {
      ListItem(
        icon = { "ic_export_$exportTypeName.svg".fromResToIcon() },
        text = { Text(text) },
        modifier = Modifier.clickable {
          chosenExportType = exportType
        }
      )
    }
  }
  
  if (chosenExportType != ExportType.NULL) {
    ExportResultHandler.exportResultAsOrderType(
      chosenExportType,
      headers,
      bodyData,
      isDark,
      onSuccess = {
        scope.launch {
          bottomSheetState.hide()
          it?.let {
            filePath = it
            successDialog.show()
          }
        }
      },
      onFailure = {
        exceptionMsg = it.message.toString()
        errorDialog.show()
      }
    )
    chosenExportType = ExportType.NULL
  }
  
}