package com.fleey.allpairs.util

import java.util.*

object EnvUtil {
  private val osName: String
    get() = System.getProperty("os.name", "generic")
  
  fun isOrderEnvType(envType: EnvType) = when (envType) {
    EnvType.MAC -> osName.lowercase(Locale.getDefault()).contains("mac")
    EnvType.WINDOWS -> osName.contains("indows")
    EnvType.LINUX -> osName.contains("nix") || osName.contains("nux") || osName.contains("aix")
  }
}

enum class EnvType {
  MAC,
  WINDOWS,
  LINUX
}