package com.fleey.allpairs.ui.component

import androidx.compose.runtime.*
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame


@Composable
fun SaveFileDialog(
  suggestFileName: String,
  suggestFileDir: String = "./",
  onFileSelected: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var isOpen by remember { mutableStateOf(true) }
  
  if (isOpen) {
    AwtWindow(
      create = {
        FileDialog(Frame(), "保存文件至", FileDialog.SAVE).apply {
          directory = suggestFileDir
          file = suggestFileName
          isVisible = true
          if (file != null) {
            onFileSelected(directory + file)
          } else {
            onDismiss()
          }
          dispose()
        }
      },
      dispose = FileDialog::dispose
    )
    isOpen = false
  }
}