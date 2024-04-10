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

package com.fleey.viewmodel.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

abstract class ViewModel {
  interface Factory
  
  /**
   * Called when the ViewModel is no longer used and will be destroyed.
   */
  open fun onCleared() {}
}

/**
 * A base ViewModel class that provides a CoroutineScope for coroutine management.
 * Subclasses can use the provided [scope] for launching coroutines.
 */
abstract class CoroutineViewModel(
  private val scope: CoroutineScope
) : ViewModel()

/**
 * The default implementation of ViewModel.Factory interface.
 * This factory can be used to create ViewModel instances.
 */
object DefaultViewModelFactory : ViewModel.Factory

/**
 * Provides a ViewModel instance of type [VM] based on the specified [scope].
 * If [VM] is a subtype of [CoroutineViewModel], it creates the ViewModel using the provided [scope].
 * Otherwise, it creates the ViewModel without any arguments.
 */
inline fun <reified VM : ViewModel> ViewModel.Factory.get(
  scope: CoroutineScope
): VM {
  return runCatching {
    VM::class.java.let { vmClass ->
      if (CoroutineViewModel::class.java.isAssignableFrom(vmClass)) {
        vmClass.getConstructor(CoroutineScope::class.java).newInstance(scope)
      } else {
        vmClass.getConstructor().newInstance()
      }
    }
  }.getOrElse {
    throw IllegalStateException("Create Instance Failure (${VM::class.java})")
  } as VM
}

/**
 * Remembers a ViewModel instance of type [VM].
 * If [factory] is not provided, the [DefaultViewModelFactory] is used to get the ViewModel instance.
 * The ViewModel instance is associated with the provided [scope].
 * The ViewModel's [ViewModel.onCleared] method is called when this Composable is disposed.
 */
@Composable
inline fun <reified VM : ViewModel> rememberViewModel(
  factory: ViewModel.Factory? = null,
  scope: CoroutineScope = rememberCoroutineScope()
): VM {
  val vm = remember { (factory ?: DefaultViewModelFactory).get<VM>(scope) }
  DisposableEffect(Unit) { onDispose { vm.onCleared() } }
  return vm
}