package com.fleey.allpairs.data.entity

import java.util.*

data class Param(
  val id: Int,
  val name: String = "",
  val values: SortedSet<String> = sortedSetOf(
    ('a'..'z').shuffled().take(2).joinToString(separator = " ")
  )
) {
  fun validate(): Boolean {
    return name.isNotBlank() && values.isNotEmpty() && values.all { it.isNotBlank() }
  }
}