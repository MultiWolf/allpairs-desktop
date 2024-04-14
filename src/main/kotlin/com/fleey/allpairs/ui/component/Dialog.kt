package com.fleey.allpairs.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface DialogActions {
  fun show()
  fun hide()
}

@Composable
fun CustomDialog(
  title: String,
  width: Int,
  height: Int,
  resizable: Boolean,
  scope: CoroutineScope,
  content: @Composable (DialogActions) -> Unit
): DialogActions {
  var isVisible by remember { mutableStateOf(false) }
  
  val actions = remember {
    object : DialogActions {
      override fun show() {
        scope.launch {
          isVisible = true
        }
      }
      
      override fun hide() {
        scope.launch {
          isVisible = false
        }
      }
    }
  }
  
  if (isVisible) {
    DialogWindow(
      onCloseRequest = { actions.hide() },
      title = title,
      state = DialogState(
        WindowPosition(Alignment.Center),
        DpSize(width.dp, height.dp)
      ),
      resizable = resizable,
    ) {
      content(actions)
    }
  }
  
  return actions
}

@Composable
fun NotificationDialog(
  dialogType: DialogType = DialogType.UNKNOWN,
  showIcon: Boolean = true,
  title: String = dialogTypeTitle(dialogType),
  subTitle: String = "",
  windowTitle: String = title,
  width: Int = 360,
  height: Int = 270,
  resizeable: Boolean = false,
  positiveText: String = "确认",
  scope: CoroutineScope = rememberCoroutineScope(),
  onPositive: ((DialogActions) -> Unit)? = null,
): DialogActions {
  val dialog = CustomDialog(
    windowTitle,
    width,
    height,
    resizeable,
    scope,
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colors.surface,
      shape = MaterialTheme.shapes.medium
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (dialogType != DialogType.UNKNOWN && showIcon) DialogTypeIcon(dialogType)
        Text(title, fontSize = 18.sp)
        Text(
          subTitle,
          fontSize = 14.sp,
          textAlign = TextAlign.Center
        )
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
          if (onPositive != null) {
            Button(
              onClick = { onPositive(it) },
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
  
  return dialog
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
    DialogType.SUCCESS -> Pair(Icons.Rounded.CheckCircle, MaterialTheme.colors.primary)
    DialogType.ERROR -> Pair(Icons.Rounded.Error, MaterialTheme.colors.error)
    DialogType.WARNING -> Pair(Icons.Rounded.Warning, MaterialTheme.colors.error)
    DialogType.UNKNOWN -> Pair(Icons.Rounded.Egg, MaterialTheme.colors.error)
  }
  Icon(
    pair.first,
    dialogType.name,
    Modifier.size(54.dp),
    pair.second
  )
}

private fun dialogTypeTitle(dialogType: DialogType): String {
  return when (dialogType) {
    DialogType.INFO -> "信息"
    DialogType.SUCCESS -> "成功"
    DialogType.ERROR -> "错误"
    DialogType.WARNING -> "警告"
    DialogType.UNKNOWN -> "未知"
  }
}