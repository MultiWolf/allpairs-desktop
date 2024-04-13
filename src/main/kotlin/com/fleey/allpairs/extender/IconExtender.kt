package com.fleey.allpairs.extender

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun String.fromResToIcon(
  size: Int = 32,
  desc: String? = null
) {
  Icon(
    painter = painterResource(this),
    contentDescription = desc,
    modifier = Modifier.size(size.dp)
  )
}