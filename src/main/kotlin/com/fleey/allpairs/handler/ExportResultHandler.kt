package com.fleey.allpairs.handler

import androidx.compose.runtime.Composable
import com.fleey.allpairs.data.entity.AllPairsItem
import com.fleey.allpairs.util.ClipboardUtil

object ExportResultHandler {
  @Composable
  fun exportResultAsOrderType(
    exportType: ExportType,
    headers: List<String>,
    bodyData: List<AllPairsItem>,
  ) {
    when (exportType) {
      ExportType.EXCEL -> {
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
      }
      
    }
  }
}

enum class ExportType {
  EXCEL,
  TEXT
}