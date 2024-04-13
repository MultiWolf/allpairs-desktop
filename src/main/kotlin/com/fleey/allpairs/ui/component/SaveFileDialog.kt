package com.fleey.allpairs.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.fleey.allpairs.util.FileUtil
import kotlinx.coroutines.launch
import java.awt.FileDialog
import java.awt.Frame
import java.io.ByteArrayOutputStream
import java.io.IOException


@Composable
fun SaveFileDialog(
  suggestFileName: String,
  suggestFileDir: String = "./",
  dataOutputStream: ByteArrayOutputStream,
  onSuccess: () -> Unit = {},
  onFailure: (IOException) -> Unit = {},
  onDismiss: () -> Unit = {}
) {
  val coroutineScope = rememberCoroutineScope()
  LaunchedEffect(suggestFileName, suggestFileDir) {
    val fileDialog = FileDialog(Frame(), "保存文件至", FileDialog.SAVE).apply {
      directory = suggestFileDir
      file = suggestFileName
    }
    fileDialog.isVisible = true
    if (fileDialog.file != null) {
      coroutineScope.launch {
        try {
          val filePath = fileDialog.directory + fileDialog.file
          FileUtil.writeFile(filePath, dataOutputStream.toByteArray(),
            onSuccess = onSuccess,
            onFailure = { onFailure(it) })
        } catch (e: IOException) {
          onFailure(e)
        }
      }
    } else {
      onDismiss()
    }
    fileDialog.dispose()
  }
}