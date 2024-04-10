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

/**
 * Creates a DataRoomListState with the provided key, initial value, save strategy, and DataRoom implementation.
 *
 * @param T The type of elements in the list.
 * @param key The key to uniquely identify the data in the DataRoom.
 * @param initialValue The initial value for the DataRoomListState.
 * @param saveStrategy The strategy for saving data changes.
 * @param room The DataRoom implementation to use for serialization.
 * @return A DataRoomListState instance.
 */
@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> dataRoomListStateOf(
  key: String,
  initialValue: List<T> = emptyList(),
  saveStrategy: SaveStrategy = SaveStrategy.NOW,
  room: DataRoom<List<T>> = DataRoom(
    { ProtoBuf.encodeToByteArray(it) },
    { ProtoBuf.decodeFromByteArray(it) }
  )
): DataRoomListState<T> {
  val data = try {
    room.read(key, initialValue)
  } catch (_: Exception) {
    initialValue
  }
  
  return DataRoomListState(room, key, data, saveStrategy)
}

/**
 * Represents a state holder for a list of data that is stored in a DataRoom.
 *
 */
class DataRoomListState<T>(
  private val room: DataRoom<List<T>>,
  private val key: String,
  private val initialValue: List<T> = emptyList(),
  private val saveStrategy: SaveStrategy = SaveStrategy.NOW,
  private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : MutableState<List<T>> by mutableStateOf(initialValue),
  MutableList<T> by ArrayList(initialValue) {
  private val list = mutableStateOf(initialValue)
  
  override var value: List<T>
    get() = list.value
    set(value) {
      doSetValue(value)
    }
  
  override fun component1(): List<T> = value
  
  override fun component2(): (List<T>) -> Unit = ::doSetValue
  
  private fun doSetValue(value: List<T>) {
    value.let {
      list.value = it
      if (saveStrategy == SaveStrategy.NOW) {
        scope.launch {
          room.save(key, it)
        }
      }
    }
  }
  
  override val size: Int
    get() = list.value.size
  
  override fun clear() = doSetValue(emptyList())
  
  override fun get(index: Int): T = list.value[index]
  
  override fun isEmpty(): Boolean = list.value.isEmpty()
  
  override fun iterator(): MutableIterator<T> = list.value.toMutableList().iterator()
  
  override fun listIterator(): MutableListIterator<T> = list.value.toMutableList().listIterator()
  
  override fun listIterator(index: Int): MutableListIterator<T> =
    list.value.toMutableList().listIterator(index)
  
  override fun removeAt(index: Int): T {
    val removed = list.value[index]
    doSetValue(list.value.toMutableList().apply { removeAt(index) })
    return removed
  }
  
  override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> =
    list.value.subList(fromIndex, toIndex).toMutableList()
  
  override fun set(index: Int, element: T): T {
    val old = list.value[index]
    doSetValue(list.value.toMutableList().apply { set(index, element) })
    return old
  }
  
  override fun retainAll(elements: Collection<T>): Boolean {
    doSetValue(list.value.filter { it in elements })
    return true
  }
  
  override fun removeAll(elements: Collection<T>): Boolean {
    doSetValue(list.value.filter { it !in elements })
    return true
  }
  
  override fun remove(element: T): Boolean {
    doSetValue(list.value.filter { it != element })
    return true
  }
  
  override fun lastIndexOf(element: T): Int = list.value.lastIndexOf(element)
  
  override fun indexOf(element: T): Int = list.value.indexOf(element)
  
  override fun containsAll(elements: Collection<T>): Boolean = list.value.containsAll(elements)
  
  override fun contains(element: T): Boolean = list.value.contains(element)
  
  override fun add(element: T): Boolean {
    doSetValue(list.value.toMutableList().apply { add(element) })
    return true
  }
  
  override fun add(index: Int, element: T) {
    doSetValue(list.value.toMutableList().apply { add(index, element) })
  }
  
  override fun addAll(elements: Collection<T>): Boolean {
    doSetValue(list.value.toMutableList().apply { addAll(elements) })
    return true
  }
  
  override fun addAll(index: Int, elements: Collection<T>): Boolean {
    doSetValue(list.value.toMutableList().apply { addAll(index, elements) })
    return true
  }
  
}