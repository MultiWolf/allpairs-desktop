package com.fleey.allpairs.data.state

import com.fleey.allpairs.handler.ExportType

data class ExportState(
  var filePath: String = "",
  var errorMessage: String = "",
  var exportType: ExportType = ExportType.NULL
)