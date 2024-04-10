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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.reflect.KProperty

/**
 * Creates a DataRoomState with the provided initial value and DataRoom implementation.
 *
 * @param T The type of data to be stored in the DataRoom.
 * @param initialValue The initial value for the DataRoomState.
 * @param room The DataRoom implementation to use for serialization.
 * @return A DataRoomState instance.
 */
@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> dataRoomStateOf(
  initialValue: T,
  room: DataRoom<T> = DataRoom(
    { ProtoBuf.encodeToByteArray(it) },
    { ProtoBuf.decodeFromByteArray(it) }
  )
): DataRoomState<T> = DataRoomState(room, initialValue)

/**
 * Represents a state holder for a single data value that is stored in a DataRoom.
 */
class DataRoomState<T>(
  private val room: DataRoom<T>,
  private val initialValue: T,
  private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : MutableState<T> {
  
  private lateinit var key: String
  private val state: MutableState<T> by lazy {
    mutableStateOf(
      try {
        room.read(key, initialValue)
      } catch (_: Exception) {
        initialValue
      }
    )
  }
  
  override var value: T
    get() = state.value
    set(_) {
      throw IllegalArgumentException("DataRoomState value can't be set directly, use delegated property instead")
    }
  
  override fun component1(): T = value
  
  override fun component2(): (T) -> Unit {
    throw IllegalArgumentException("DataRoomState can't be deconstructed, use delegated property instead")
  }
  
  /**
   * Custom operator function to set the value of the DataRoomState.
   * If the key is not initialized, it generates a key using DRKeyGenerator.
   * Saves the new value in the DataRoom and removes it if the value is null.
   *
   * @param thisObj The object reference.
   * @param property The property being set.
   * @param value The new value to set.
   */
  operator fun setValue(thisObj: Any?, property: KProperty<*>, value: T) {
    if (!::key.isInitialized) {
      key = DRKeyGenerator.generate(thisObj, property)
    }
    state.value = value
    value?.let {
      scope.launch {
        room.save(key, it)
      }
    } ?: run {
      room.remove(key)
    }
  }
}