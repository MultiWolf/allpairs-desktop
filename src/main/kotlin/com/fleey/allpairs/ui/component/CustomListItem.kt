package com.fleey.allpairs.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fleey.allpairs.extender.fromResToIcon
import com.fleey.allpairs.handler.ExportType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomListItem(
  iconPath: String? = null,
  text: String,
  secondaryText: String? = null,
  onClick: () -> Unit
) {
  ListItem(
    icon = iconPath?.let { { "ic_$iconPath.svg".fromResToIcon() } },
    text = { Text(text) },
    secondaryText = secondaryText?.let { { Text(it) } },
    modifier = Modifier.clickable(onClick = onClick)
  )
}

@Composable
fun ExportTypeListItem(
  exportTypes: List<ExportType>,
  onExportSelected: (ExportType) -> Unit
) {
  exportTypes.forEach { exportType ->
    if (exportType == ExportType.NULL) return@forEach
    val exportTypeName = exportType.name.lowercase()
    val text = getExportTypeText(exportType)
    val secondaryText = getExportTypeSecondaryText(exportType)
    
    CustomListItem(
      iconPath = "export_$exportTypeName",
      text = text,
      secondaryText = secondaryText,
      onClick = { onExportSelected(exportType) }
    )
  }
}

fun getExportTypeText(exportType: ExportType): String = when (exportType) {
  ExportType.EXCEL -> "表格"
  ExportType.HTML -> "网页"
  ExportType.IMAGE -> "图片"
  ExportType.TEXT -> "纯文字"
  ExportType.MARKDOWN -> "Markdown"
  else -> ""
}

fun getExportTypeSecondaryText(exportType: ExportType): String? = when (exportType) {
  ExportType.EXCEL -> "以 .xlsx 类型保存"
  ExportType.IMAGE -> "以 .png 类型保存"
  ExportType.HTML -> "以 .html 网页呈现"
  ExportType.TEXT -> "复制至剪贴板，可粘贴至 excel"
  ExportType.MARKDOWN -> "复制至剪贴板，可粘贴至 md 编辑器"
  else -> null
}
