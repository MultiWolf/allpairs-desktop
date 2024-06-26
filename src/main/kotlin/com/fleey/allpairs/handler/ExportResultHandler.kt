package com.fleey.allpairs.handler

import androidx.compose.runtime.Composable
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import com.fleey.allpairs.data.entity.AllPairsItem
import com.fleey.allpairs.extender.ResExtender.fromResToText
import com.fleey.allpairs.ui.common.theme.AppTheme
import com.fleey.allpairs.ui.component.ResultTable
import com.fleey.allpairs.ui.component.SaveFileDialog
import com.fleey.allpairs.util.ClipboardUtil
import com.fleey.allpairs.util.ExcelUtil
import kotlinx.coroutines.Dispatchers
import org.jetbrains.skia.EncodedImageFormat
import java.io.ByteArrayOutputStream
import java.io.IOException

object ExportResultHandler {
  @Composable
  fun exportResultAsOrderType(
    exportType: ExportType,
    headers: List<String>,
    bodyData: List<AllPairsItem>,
    isDark: Boolean,
    onSuccess: (String?) -> Unit = {},
    onFailure: (IOException) -> Unit = {},
  ) {
    when (exportType) {
      ExportType.EXCEL -> {
        val excelBody = listOf(headers) + bodyData.map { item ->
          listOf<Any>(item.index) + item.values
        }
        
        ExcelUtil().addData("Result Sheet", excelBody.toList()).saveAsExcel(
          "allpairs.xlsx",
          onSuccess = { onSuccess(it) },
          onFailure = { onFailure(it) }
        )
      }
      
      ExportType.HTML -> {
        val htmlDataOutputSteam =
          exportToHTMLDataOutputSteam(headers, bodyData, "style.css")
        
        SaveFileDialog(
          "allpairs.html",
          dataOutputStream = htmlDataOutputSteam,
          onSuccess = { onSuccess(it) },
          onFailure = { onFailure(it) }
        )
      }
      
      ExportType.IMAGE -> {
        exportTableAsImage(
          isDark,
          headers,
          bodyData,
          onSuccess,
          onFailure
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
        onSuccess(null)
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
        onSuccess(null)
      }
      
      ExportType.NULL -> {}
    }
  }
}

@Composable
private fun exportToHTMLDataOutputSteam(
  headers: List<String>,
  bodyData: List<AllPairsItem>,
  cssFilename: String
): ByteArrayOutputStream {
  val htmlStr = generateFullHTML(headers, bodyData, cssFilename)
  val dataOutputStream = ByteArrayOutputStream().apply {
    write(htmlStr.toByteArray())
  }
  
  return dataOutputStream
}

private fun generateHTMLHead(cssFilename: String): String = buildString {
  val cssStr = cssFilename.fromResToText()
  append(
    """

    <head>
      <meta charset='UTF-8'>
      <meta name='viewport' content='width=device-width initial-scale=1'>
      <title>allpairs-result</title>
      <style>
    $cssStr
      </style>
    </head>
        """.trimIndent()
  )
}

private fun generateTableHeader(headers: List<String>): String = buildString {
  append("<thead><tr>")
  headers.forEach { append("<th>$it</th>") }
  append("</tr></thead>")
}

private fun generateTableBody(bodyData: List<AllPairsItem>): String = buildString {
  append("<tbody>")
  bodyData.forEach { row ->
    append("<tr><td>${row.index}</td>")
    row.values.forEach { value ->
      append("<td>$value</td>")
    }
    append("</tr>")
  }
  append("</tbody>")
}

private fun generateFullHTML(
  headers: List<String>,
  bodyData: List<AllPairsItem>,
  cssFilename: String
): String = buildString {
  append("<html>")
  append(generateHTMLHead(cssFilename))
  append("<body><div><figure><table>")
  append(generateTableHeader(headers))
  append(generateTableBody(bodyData))
  append("</table></figure></div></body>")
  append("</html>")
}

@Composable
private fun exportTableAsImage(
  isDark: Boolean,
  headers: List<String>,
  bodyData: List<AllPairsItem>,
  onSuccess: (String) -> Unit = {},
  onFailure: (IOException) -> Unit = {},
) {
  val width = headers.sumOf { it.length } * 8.5 + headers.size * 89.7
  val height = bodyData.size * 33.9
  
  val scene = ImageComposeScene(
    width = width.toInt(),
    height = height.toInt(),
    density = Density(1f),
    coroutineContext = Dispatchers.Unconfined
  ) {
    AppTheme(isDark) {
      ResultTable(headers, bodyData)
    }
  }
  
  val image = scene.render()
  image.use { img ->
    img.encodeToData(EncodedImageFormat.PNG)?.let { data ->
      val dataOutputStream = ByteArrayOutputStream().apply {
        write(data.bytes)
      }
      
      SaveFileDialog(
        suggestFileName = "allpairs.png",
        dataOutputStream = dataOutputStream,
        onSuccess = onSuccess,
        onFailure = { onFailure(it) },
      ) { }
    }
    
  }
}

enum class ExportType {
  EXCEL,
  HTML,
  IMAGE,
  MARKDOWN,
  TEXT,
  NULL
}