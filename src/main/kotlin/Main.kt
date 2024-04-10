import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fleey.allpairs.data.config.AppConfig
import com.fleey.allpairs.ui.common.theme.AppTheme
import com.fleey.allpairs.ui.main.AppMain
import com.fleey.allpairs.util.EnvType
import com.fleey.allpairs.util.EnvUtil
import java.awt.Dimension


/**
 * @author fleey
 * @date 2024-04-11 04:11
 */
fun main() = application {
  val windowState = rememberWindowState(
    size = DpSize(
      AppConfig.WINDOW_MIN_WIDTH.dp,
      AppConfig.WINDOW_MIN_HEIGHT.dp
    )
  )
  
  Window(
    state = windowState,
    onCloseRequest = ::exitApplication,
    title = "Allpairs",
    resizable = true,
    undecorated = EnvUtil.isOrderEnvType(EnvType.WINDOWS)
  ) {
    window.minimumSize = Dimension(AppConfig.WINDOW_MIN_WIDTH, AppConfig.WINDOW_MIN_HEIGHT)
    
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