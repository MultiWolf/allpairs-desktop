package com.fleey.allpairs.extender

object ResExtender {
  private val classLoader by lazy { javaClass.classLoader }
  
  fun String.fromResToText() = classLoader.getResourceAsStream(this).toText()
}