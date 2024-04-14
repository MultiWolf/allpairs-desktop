package com.fleey.allpairs.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition


@Composable
fun CustomDialog(
  title: String,
  width: Int,
  height: Int,
  resizable: Boolean = false,
  onDismissRequest: () -> Unit,
  content: @Composable () -> Unit
) {
  DialogWindow(
    onCloseRequest = onDismissRequest,
    title = title,
    state = DialogState(
      WindowPosition(Alignment.Center),
      DpSize(width.dp, height.dp)
    ),
    resizable = false,
  ) {
    content()
  }
}

@Composable
fun BaseDialog(
  dialogType: DialogType = DialogType.UNKNOWN,
  showIcon: Boolean = true,
  title: String,
  subTitle: String = "",
  windowTitle: String = title,
  width: Int = 320,
  height: Int = 210,
  resizeable: Boolean = false,
  onNegative: () -> Unit,
  positiveText: String = "确认",
  onPositive: (() -> Unit)? = null,
) {
  CustomDialog(
    windowTitle,
    width,
    height,
    resizeable,
    onNegative,
  ) {
    val emptyEvent: () -> Unit
    
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colors.surface,
      shape = MaterialTheme.shapes.medium
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        if (dialogType != DialogType.UNKNOWN && showIcon) DialogTypeIcon(dialogType)
        Text(title, style = MaterialTheme.typography.h6)
        Text(subTitle, style = MaterialTheme.typography.body2)
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
          if (onPositive != null) {
            Button(
              onPositive,
              modifier = Modifier.weight(1f),
              elevation = ButtonDefaults.elevation(0.dp)
            ) {
              Text(positiveText)
            }
          }
        }
      }
    }
  }
}

enum class DialogType {
  INFO,
  SUCCESS,
  ERROR,
  WARNING,
  UNKNOWN
}

@Composable
fun DialogTypeIcon(dialogType: DialogType) {
  val pair = when (dialogType) {
    DialogType.INFO -> Pair(Icons.Rounded.Info, MaterialTheme.colors.primary)
    DialogType.SUCCESS -> Pair(Icons.Rounded.Done, MaterialTheme.colors.primary)
    DialogType.ERROR -> Pair(Icons.Rounded.Error, MaterialTheme.colors.error)
    DialogType.WARNING -> Pair(Icons.Rounded.Warning, MaterialTheme.colors.error)
    DialogType.UNKNOWN -> Pair(Icons.Rounded.Egg, MaterialTheme.colors.error)
  }
  Icon(
    pair.first,
    dialogType.name,
    Modifier.size(48.dp),
    pair.second
  )
}