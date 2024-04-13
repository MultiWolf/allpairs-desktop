package com.fleey.allpairs.handler

import androidx.compose.runtime.Composable
import com.fleey.allpairs.data.entity.AllPairsItem
import com.fleey.allpairs.util.ClipboardUtil
import com.fleey.allpairs.util.ExcelUtil

object ExportResultHandler {
  @Composable
  fun exportResultAsOrderType(
    exportType: ExportType,
    headers: List<String>,
    bodyData: List<AllPairsItem>,
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {},
  ) {
    when (exportType) {
      ExportType.EXCEL -> {
        val excelBody = listOf(headers) + bodyData.map { item ->
          listOf<Any>(item.index) + item.values
        }
        
        ExcelUtil.addData("Result Sheet", excelBody.toList()).saveAsExcel(
          "allpairs.xlsx",
          onSuccess = { onSuccess() },
          onFailure = { onFailure() }
        )
      }
      
      ExportType.TEXT -> {
        val bodyContent = buildString {
          append(headers.joinToString("\t"))
          appendLine()
          
          bodyData.forEach {
            append("${it.index}\t${it.values.joinToString("\t")}")
            appendLine()
          }
        }
        ClipboardUtil.CopyTextToClipboard(bodyContent)
        onSuccess()
      }
      
      ExportType.MARKDOWN -> {
        val markdownContent = buildString {
          append("| ")
          append(headers.joinToString(" | "))
          append(" |")
          appendLine()
          
          append("|")
          repeat(headers.size) { append(" ---- |") }
          appendLine()
          
          bodyData.forEach { item ->
            append("| ")
            append(item.index)
            item.values.joinToString(" | ", " | ", " |").also { append(it) }
            appendLine()
          }
        }
        ClipboardUtil.CopyTextToClipboard(markdownContent)
        onSuccess()
      }
      
      ExportType.NULL -> {}
    }
  }
}

enum class ExportType {
  EXCEL,
  TEXT,
  MARKDOWN,
  NULL
}