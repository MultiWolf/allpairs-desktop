package com.fleey.allpairs.extender

import com.fleey.allpairs.data.entity.Param
import io.github.pavelicii.allpairs4j.Parameter

fun List<Param>.toParameters(): List<Parameter> = map { Parameter(it.name, it.values.toList()) }

fun List<Param>.validate(): Boolean =
  size >= 2 && all { it.validate() } && map { it.name }.distinct().size == size