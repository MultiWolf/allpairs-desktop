package com.fleey.allpairs.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

object FileUtil {
  suspend fun readFile(
    filePath: String,
    onSuccess: (ByteArray) -> Unit,
    onFailure: (IOException) -> Unit
  ) = withContext(Dispatchers.IO) {
    try {
      BufferedInputStream(FileInputStream(filePath)).use { inputStream ->
        val data = inputStream.readBytes()
        onSuccess(data)
      }
    } catch (e: IOException) {
      onFailure(e)
    }
  }
  
  suspend fun writeFile(
    filePath: String,
    data: ByteArray,
    onSuccess: () -> Unit,
    onFailure: (IOException) -> Unit
  ) = withContext(Dispatchers.IO) {
    try {
      BufferedOutputStream(FileOutputStream(filePath)).use { outputStream ->
        outputStream.write(data)
      }
      onSuccess()
    } catch (e: IOException) {
      onFailure(e)
    }
  }
  
  suspend fun readLargeFile(
    filePath: String,
    chunkSize: Int = 4096,
    onChunkRead: (ByteArray, Int) -> Unit,
    onComplete: () -> Unit,
    onFailure: (IOException) -> Unit
  ) = withContext(Dispatchers.IO) {
    try {
      BufferedInputStream(FileInputStream(filePath)).use { inputStream ->
        val buffer = ByteArray(chunkSize)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
          onChunkRead(buffer, bytesRead)
        }
      }
      onComplete()
    } catch (e: IOException) {
      onFailure(e)
    }
  }
  
  suspend fun writeLargeFile(
    filePath: String,
    data: ByteArray,
    chunkSize: Int = 4096,
    onSuccess: () -> Unit,
    onFailure: (IOException) -> Unit
  ) = withContext(Dispatchers.IO) {
    try {
      FileOutputStream(filePath).use { fos ->
        data.inputStream().use { inputStream ->
          val buffer = ByteArray(chunkSize)
          var read: Int
          while (inputStream.read(buffer).also { read = it } != -1) {
            fos.write(buffer, 0, read)
          }
        }
      }
      onSuccess()
    } catch (e: IOException) {
      onFailure(e)
    }
  }
  
}