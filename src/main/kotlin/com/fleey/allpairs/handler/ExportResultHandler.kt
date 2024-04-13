package com.fleey.allpairs.handler

object ExportResultHandler {
  fun exportResultAsOrderType(exportType: ExportType) {
    when (exportType) {
      ExportType.EXCEL -> TODO()
      ExportType.TXT -> TODO()
    }
  }
}

enum class ExportType {
  EXCEL,
  TXT
}