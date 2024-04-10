package com.fleey.allpairs.data.entity

import java.util.*

data class Param(
  val name: String = "",
  val values: SortedSet<String> = sortedSetOf()
) {
  fun validate(): Boolean {
    return name.isNotBlank() && values.isNotEmpty() && values.all { it.isNotBlank() }
  }
}
