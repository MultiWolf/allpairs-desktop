import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
  
  MaterialTheme(themes[isDark]!!) {
    CustomWindow(
      state,
      windowMinWidth,
      windowMinHeight,
      true,
      0,
      { exitApplication() }) {
      WindowCenter {
        WindowTitle("")
        Row(Modifier.fillMaxWidth()) {
          Icon(
            imageVector = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
            modifier = Modifier
              .windowFrameItem("theme", HitSpots.OTHER_HIT_SPOT)
              .clickable { isDark = !isDark }
              .padding(5.dp)
              .size(18.dp)
              .clip(CircleShape),
            contentDescription = "toggle light/dark theme",
          )
        }
      }
      AppMain(isDark)
    }
  }
}