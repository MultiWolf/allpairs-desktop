/*
 * Copyright (c) 2024-present. Fleey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fleey.dataroom

import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * DataRoom class that implements IDataRoom interface for storing and retrieving data of type T.
 *
 * @param T The type of data to be stored in the DataRoom.
 * @property save Function to serialize the data of type T to ByteArray.
 * @property load Function to deserialize the ByteArray back to type T.
 */
class DataRoom<T>(
  private val save: (T) -> ByteArray,
  private val load: (ByteArray) -> T
) : IDataRoom<T> {
  companion object {
    private lateinit var path: String
    
    /**
     * Initialize the path for storing data files.
     *
     * @param path The directory path where data files will be saved.
     */
    fun init(path: String) {
      if (::path.isInitialized) {
        throw IllegalStateException("Path has already been initialized")
      }
      this.path = path
    }
  }
  
  private val lock = ReentrantReadWriteLock()
  
  /**
   * Save the data of type T to a file with the specified key.
   *
   * @param key The unique key to identify the data.
   * @param data The data of type T to be saved.
   */
  override fun save(key: String, data: T) {
    lock.write { File(path, key).writeBytes(save(data)) }
  }
  
  /**
   * Read and load the data of type T from the file with the specified key.
   *
   * @param key The unique key to identify the data.
   * @param default The default value of type T if data is not found.
   * @return The loaded data of type T or the default value if not found.
   */
  override fun read(key: String, default: T): T {
    return lock.read {
      val file = File(path, key)
      if (file.exists()) load(file.readBytes()) else default
    }
  }
  
  /**
   * Remove the data file associated with the specified key.
   *
   * @param key The unique key to identify the data file to be removed.
   */
  override fun remove(key: String) {
    File(path, key).delete()
  }
}