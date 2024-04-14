package com.fleey.allpairs.extender

import java.io.InputStream

fun InputStream?.toText() = this?.reader()?.readText() ?: ""