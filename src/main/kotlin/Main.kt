import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fleey.allpairs.data.config.windowMinHeight
import com.fleey.allpairs.data.config.windowMinWidth
import com.fleey.allpairs.ui.common.theme.themes
import com.fleey.allpairs.ui.main.AppMain
import com.fleey.allpairs.util.EnvType
import com.fleey.allpairs.util.EnvUtil
import com.fleey.customwindow.*

/**
 * @author fleey
 * @date 2024-04-11 04:11
 */
fun main() = application {
  val state = rememberWindowState(
    size = DpSize(
      windowMinWidth.dp,
      windowMinHeight.dp
    )
  )
  val isDarkMode = isSystemInDarkTheme()
  var isDark by remember { mutableStateOf(isDarkMode) }
  val isMacEnv = EnvUtil.isOrderEnvType(EnvType.MAC)
  
  val windowTitleStartPadding = if (isMacEnv) 64 else 0
  
  MaterialTheme(themes[isDark]!!) {
    CustomWindow(
      state,
      windowMinWidth,
      windowMinHeight,
      !isMacEnv,
      windowTitleStartPadding,
      { exitApplication() }) {
      WindowCenter {
        WindowTitle("")
        Row(Modifier.fillMaxWidth()) {
          if (isMacEnv) Spacer((Modifier.weight(1f)))
          Icon(
            imageVector = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
            modifier = Modifier
              .windowFrameItem("theme", HitSpots.CLOSE_BUTTON)
              .clickable { isDark = !isDark }
              .padding(8.dp)
              .size(20.dp)
              .clip(CircleShape),
            contentDescription = "toggle light/dark theme",
          )
        }
      }
      AppMain(isDark)
    }
  }
}