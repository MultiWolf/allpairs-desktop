package com.fleey.allpairs.extender

import com.fleey.allpairs.util.EnvType
import com.fleey.allpairs.util.EnvUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

fun File.ensureParentDirExists(): Boolean {
  val parent = this.parentFile
  return if (!parent.exists()) parent.mkdirs() else parent.isDirectory
}

suspend fun File.readBytesAsync(): ByteArray? = withContext(Dispatchers.IO) {
  try {
    this@readBytesAsync.inputStream().use { it.readBytes() }
  } catch (e: IOException) {
    println("Error reading file asynchronously: ${e.message}")
    null
  }
}


fun File.toByteArray(): ByteArray? {
  return try {
    this.inputStream().use { it.readBytes() }
  } catch (e: IOException) {
    println("Error reading file to byte array: ${e.message}")
    null
  }
}

fun File.toText(): String? {
  return try {
    this.readText()
  } catch (e: IOException) {
    println("Error reading file to text: ${e.message}")
    null
  }
}

fun String.openFileInEnv() {
  val envType = EnvUtil.getEnvType()
  when (envType) {
    EnvType.MAC -> openFileInFinder()
    EnvType.WINDOWS -> openFileInExplorer()
    EnvType.LINUX -> openFileInFileManager()
    EnvType.UNKNOWN -> throw IllegalStateException(
      "Unsupported or unknown operating system: ${
        System.getProperty(
          "os.name"
        )
      }"
    )
  }
}

private fun String.openFileInExplorer() {
  try {
    Runtime.getRuntime().exec("explorer.exe /select,${this.replace('/', '\\')}")
  } catch (e: IOException) {
    throw IllegalStateException("Failed to open file in Windows Explorer: $this", e)
  }
}

private fun String.openFileInFinder() {
  try {
    Runtime.getRuntime().exec(arrayOf("open", "-R", this))
  } catch (e: IOException) {
    throw IllegalStateException("Failed to open file in macOS Finder: $this", e)
  }
}

private fun String.openFileInFileManager() {
  try {
    Runtime.getRuntime().exec(arrayOf("xdg-open", this))
  } catch (e: IOException) {
    throw IllegalStateException("Failed to open file in Linux file manager: $this", e)
  }
}