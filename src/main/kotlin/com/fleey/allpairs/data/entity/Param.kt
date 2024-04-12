package com.fleey.allpairs.data.entity

import java.util.*

data class Param(
  var id: Int,
  val name: String = "",
  val values: SortedSet<String> = sortedSetOf(),
  // 缓解 LazyColumn 的一堆奇怪问题而添加
  val random: String = UUID.randomUUID().toString()
) {
  fun validate(): Boolean {
    return name.isNotBlank() && values.isNotEmpty() && values.all { it.isNotBlank() }
  }
}