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
 *
 */

package com.fleey.dataroom

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * Creates a DataRoomMapState with the provided key, initial value, save strategy, and DataRoom implementation.
 *
 * @param K The type of keys in the map.
 * @param V The type of values in the map.
 * @param key The key to uniquely identify the data in the DataRoom.
 * @param initialValue The initial value for the DataRoomMapState.
 * @param saveStrategy The strategy for saving data changes.
 * @param room The DataRoom implementation to use for serialization.
 * @return A DataRoomMapState instance.
 */
inline fun <reified K, reified V> dataRoomMapStateOf(
  key: String,
  initialValue: Map<K, V> = emptyMap(),
  saveStrategy: SaveStrategy = SaveStrategy.NOW,
  room: DataRoom<Map<K, V>> = DataRoom(
    { ProtoBuf.encodeToByteArray(it) },
    { ProtoBuf.decodeFromByteArray(it) }
  )
): DataRoomMapState<K, V> {
  val data = try {
    room.read(key, initialValue)
  } catch (_: Exception) {
    initialValue
  }
  
  return DataRoomMapState(room, key, data, saveStrategy)
}

/**
 * Represents a state holder for a map of data that is stored in a DataRoom.
 */
class DataRoomMapState<K, V>(
  private val room: DataRoom<Map<K, V>>,
  private val key: String,
  private val initialValue: Map<K, V> = emptyMap(),
  private val saveStrategy: SaveStrategy = SaveStrategy.NOW,
  private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : MutableState<Map<K, V>> by mutableStateOf(initialValue),
  MutableMap<K, V> {
  private val map = mutableStateOf(initialValue)
  
  override var value: Map<K, V>
    get() = map.value
    set(value) {
      doSetValue(value)
    }
  
  override fun component1(): Map<K, V> = value
  
  override fun component2(): (Map<K, V>) -> Unit = ::doSetValue
  
  private fun doSetValue(value: Map<K, V>) {
    value.let {
      map.value = it
      if (saveStrategy == SaveStrategy.NOW) {
        scope.launch {
          room.save(key, value)
        }
      }
    }
  }
  
  override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
    get() = map.value.toMutableMap().entries
  
  override val keys: MutableSet<K>
    get() = map.value.keys.toMutableSet()
  
  override val size: Int
    get() = map.value.size
  
  override val values: MutableCollection<V>
    get() = map.value.values.toMutableList()
  
  override fun clear() = doSetValue(emptyMap())
  
  override fun isEmpty(): Boolean = map.value.isEmpty()
  
  override fun remove(key: K): V? = map.value.toMutableMap().remove(key)
  
  override fun putAll(from: Map<out K, V>) = doSetValue(map.value + from)
  
  override fun put(key: K, value: V): V? {
    val old = map.value[key]
    doSetValue(map.value + (key to value))
    return old
  }
  
  override fun get(key: K): V? = map.value[key]
  
  override fun containsValue(value: V): Boolean = map.value.containsValue(value)
  
  override fun containsKey(key: K): Boolean = map.value.containsKey(key)
  
  /**
   * Replace all key-value pairs in the Map with the given elements.
   * @param elements the new key-value pairs to replace the existing Map
   */
  fun replaceAll(elements: Map<K, V>) = doSetValue(elements.toMap())
  
  /**
   * Remove the data from the DataRoom and replace it with the specified replacement.
   * @param replacement the replacement Map data, default is the initial value
   */
  fun remove(replacement: Map<K, V> = initialValue) {
    room.remove(key)
    map.value = replacement
  }
  
}