package com.fleey.allpairs.util

import androidx.compose.runtime.Composable
import com.fleey.allpairs.ui.component.SaveFileDialog
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.io.IOException

class ExcelUtil {
  private val workbook = XSSFWorkbook()
  
  fun addData(
    sheetName: String,
    data: List<List<Any>>,
    customizeCell: (Cell, Any) -> Unit = { cell, value -> cell.setCellValue(value.toString()) }
  ): ExcelUtil {
    val sheet = workbook.getSheet(sheetName) ?: workbook.createSheet(sheetName)
    data.forEachIndexed { rowIndex, rowData ->
      val row = sheet.createRow(rowIndex)
      rowData.forEachIndexed { columnIndex, value ->
        val cell = row.createCell(columnIndex)
        customizeCell(cell, value)
      }
    }
    return this
  }
  
  @Composable
  fun saveAsExcel(
    suggestFIleName: String,
    onSuccess: (String) -> Unit = {},
    onFailure: (IOException) -> Unit = {}
  ) {
    val dataOutputStream = ByteArrayOutputStream().apply {
      workbook.use {
        it.write(this)
      }
    }
    
    SaveFileDialog(
      suggestFIleName,
      dataOutputStream = dataOutputStream,
      onSuccess = onSuccess,
      onFailure = { onFailure(it) }
    )
  }
}