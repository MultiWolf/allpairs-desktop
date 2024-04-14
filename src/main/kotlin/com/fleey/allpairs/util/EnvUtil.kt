package com.fleey.allpairs.util

import java.util.*

object EnvUtil {
  private val osName: String by lazy {
    System.getProperty("os.name", "generic").lowercase(Locale.getDefault())
  }
  
  fun getEnvType() = EnvType.entries.firstOrNull { isOrderEnvType(it) } ?: EnvType.UNKNOWN
  
  fun isOrderEnvType(envType: EnvType): Boolean = when (envType) {
    EnvType.MAC -> osName.contains("mac")
    EnvType.WINDOWS -> osName.contains("windows")
    EnvType.LINUX -> osName.contains("nix") || osName.contains("nux") || osName.contains("aix")
    EnvType.UNKNOWN -> false
  }
}

enum class EnvType {
  MAC,
  WINDOWS,
  LINUX,
  UNKNOWN // 添加一个未知类型以处理不可识别的环境
}