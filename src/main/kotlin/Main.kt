import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fleey.allpairs.data.config.windowMinHeight
import com.fleey.allpairs.data.config.windowMinWidth
import com.fleey.allpairs.ui.common.theme.AppTheme
import com.fleey.allpairs.ui.main.AppMain
import java.awt.Dimension


/**
 * @author fleey
 * @date 2024-04-11 04:11
 */
fun main() = application {
  val windowState = rememberWindowState(
    size = DpSize(
      windowMinWidth.dp,
      windowMinHeight.dp
    )
  )
  
  Window(
    state = windowState,
    onCloseRequest = ::exitApplication,
    title = "Allpairs",
    resizable = true,
  ) {
    window.minimumSize = Dimension(windowMinWidth, windowMinHeight)
    
    App()
  }
}

@Composable
fun App() {
  val _isDark = isSystemInDarkTheme()
  var isDark by remember { mutableStateOf(_isDark) }
  
  val toggleTheme = { isDark = !isDark }
  AppTheme(isDark) {
    AppMain(isDark, toggleTheme)
  }
}