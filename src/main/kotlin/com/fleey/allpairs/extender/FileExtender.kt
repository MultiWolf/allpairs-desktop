package com.fleey.allpairs.extender

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